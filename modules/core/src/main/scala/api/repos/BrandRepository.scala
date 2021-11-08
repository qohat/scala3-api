package api.repos

import api.domain.brands._
import java.util.UUID
import cats.effect.kernel.Ref
import cats.syntax.functor._
import cats.effect.kernel.Sync
import skunk.Session
import cats.Monad

trait BrandRepository[F[_]]:
  def findAll: F[List[Brand]]
  def findBy(id: BrandId): F[Option[Brand]]
  def save(brand: Brand): F[Unit]
  def update(id: BrandId, brand: Brand): F[Unit]
  def delete(id: BrandId): F[Unit]

final case class BrandInMemoryRepository[F[_]: Sync] private (inMemoryDB: Ref[F, Map[String, Brand]])
    extends BrandRepository[F]:
  def findAll: F[List[Brand]]               = inMemoryDB.get.map(_.values.toList)
  def findBy(id: BrandId): F[Option[Brand]] = inMemoryDB.get.map(_.get(id.value.toString))
  def save(brand: Brand): F[Unit] = findBy(brand.id) fmap {
    case None => inMemoryDB.update(_ + (brand.id.value.toString -> brand))
    case _    => Sync[F].unit
  }
  def update(id: BrandId, brand: Brand): F[Unit] = findBy(id) fmap {
    case Some(_) => inMemoryDB.update(_ + (id.value.toString -> brand))
    case _       => Sync[F].unit
  }
  def delete(id: BrandId): F[Unit] = inMemoryDB.get fmap {
    _.get(id.value.toString) match {
      case Some(_) => inMemoryDB.update(_ - id.value.toString)
      case None    => Sync[F].unit
    }
  }

object BrandInMemoryRepository:
  def make[F[_]: Sync](inMemoryDB: Ref[F, Map[String, Brand]]): F[BrandRepository[F]] =
    Sync[F].delay(new BrandInMemoryRepository[F](inMemoryDB))

final class SkunkBrandRepository[F[_]: Sync] private (session: Session[F]) extends BrandRepository[F]:
  import SkunkBrandRepository._
  def findAll: F[List[Brand]] =
    session
      .execute(all)
  def findBy(id: BrandId): F[Option[Brand]] =
    session
      .prepare(byId)
      .use(_.option(id.value.toString))
  def save(brand: Brand): F[Unit] =
    session
      .prepare(ins)
      .use(_.execute((((brand.id.value.toString, brand.name), brand.category), brand.stock), brand.author))
      .void
  def update(id: BrandId, brand: Brand): F[Unit] =
    session
      .prepare(upd)
      .use(_.execute((((brand.name, brand.category), brand.stock), brand.author), brand.id.value.toString))
      .void
  def delete(id: BrandId): F[Unit] = session.prepare(del).use(_.execute(id.value.toString)).void

object SkunkBrandRepository:
  import skunk._
  import skunk.implicits._
  import skunk.codec.all._

  def make[F[_]: Sync](session: Session[F]): F[BrandRepository[F]] =
    Sync[F].delay(new SkunkBrandRepository(session))

  def brand: Decoder[Brand] =
    (varchar ~ varchar ~ varchar ~ int4 ~ varchar)
      .map { case id ~ name ~ category ~ stock ~ author =>
        Brand(BrandId(UUID.fromString(id)), name, category, stock, author)
      }

  def all: Query[Void, Brand] =
    sql"SELECT * FROM brands".query(brand)

  def byId: Query[String, Brand] =
    sql"SELECT * FROM brands WHERE id = $varchar".query(brand)

  def ins: Command[String ~ String ~ String ~ Int ~ String] =
    sql"INSERT INTO brands ($varchar, $varchar, $varchar, $int4, $varchar)".command

  def upd: Command[String ~ String ~ Int ~ String ~ String] =
    sql"""UPDATE brands SET name = $varchar, category = $varchar, stock = $int4, author = $varchar 
      WHERE id = $varchar""".command

  def del: Command[String] =
    sql"DELETE FROM brands WHERE id = $varchar".command
