package api.services

import api.domain.brands._
import java.io.IOException
import org.http4s.CacheDirective.`private`
import api.repos.BrandRepository
import cats.effect.kernel.Sync

trait Brands[F[_]]:
  def findAll: F[List[Brand]]
  def findBy(id: BrandId): F[Option[Brand]]
  def save(brand: Brand): F[Unit]
  def update(id: BrandId, brand: Brand): F[Unit]
  def delete(id: BrandId): F[Unit]

final case class BrandService[F[_]: Sync] private (repository: BrandRepository[F]) extends Brands[F]:
  def findAll: F[List[Brand]]                    = repository.findAll
  def findBy(id: BrandId): F[Option[Brand]]      = repository.findBy(id)
  def save(brand: Brand): F[Unit]                = repository.save(brand)
  def update(id: BrandId, brand: Brand): F[Unit] = repository.update(id, brand)
  def delete(id: BrandId): F[Unit]               = repository.delete(id)

object BrandService:
  def make[F[_]: Sync](repository: BrandRepository[F]): F[Brands[F]] =
    Sync[F].delay(new BrandService[F](repository))
