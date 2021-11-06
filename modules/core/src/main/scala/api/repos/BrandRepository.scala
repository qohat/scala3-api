package api.repos

import api.domain.brands._
import java.util.UUID
import cats.effect.kernel.Ref
import cats.syntax.functor._
import cats.effect.kernel.Sync
import org.http4s.CacheDirective.`private`

trait BrandRepository[F[_]]:
  def findAll: F[List[Brand]]
  def findBy(id: BrandId): F[Option[Brand]]
  def save(brand: Brand): F[Unit]
  def update(id: BrandId, brand: Brand): F[Unit]
  def delete(id: BrandId): F[Unit]

final case class BrandInMemoryRepository[F[_]: Sync] private (inMemoryDB: Ref[F, Map[BrandId, Brand]])
    extends BrandRepository[F]:
  def findAll: F[List[Brand]]               = inMemoryDB.get.map(_.values.toList)
  def findBy(id: BrandId): F[Option[Brand]] = inMemoryDB.get.map(_.get(id))
  def save(brand: Brand): F[Unit] = findBy(brand.id) fmap {
    case None => inMemoryDB.update(_ + (brand.id -> brand))
    case _    => Sync[F].delay(())
  }
  def update(id: BrandId, brand: Brand): F[Unit] = findBy(id) fmap {
    case Some(_) => inMemoryDB.update(_ + (id -> brand))
    case _       => Sync[F].delay(())
  }
  def delete(id: BrandId): F[Unit] = inMemoryDB.get.fmap {
    _.get(id) match {
      case Some(_) => inMemoryDB.update(_ - id)
      case None    => Sync[F].delay(())
    }
  }

object BrandInMemoryRepository:
  def make[F[_]: Sync](inMemoryDB: Ref[F, Map[BrandId, Brand]]): F[BrandRepository[F]] =
    Sync[F].delay(new BrandInMemoryRepository[F](inMemoryDB))
