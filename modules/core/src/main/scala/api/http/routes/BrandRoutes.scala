package api.http.routes

import api.http.pathvars.BrandIdVar

import api.services.Brands
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import api.domain.brands.BrandId
import org.http4s.circe.CirceEntityCodec.{ circeEntityDecoder, circeEntityEncoder }
import io.circe.generic.auto._
import org.http4s.server.Router
import cats.effect.kernel.Sync

final case class BrandRoutes[F[_]: Sync] private (brands: Brands[F]) extends Http4sDsl[F]:

  private[routes] val prefixPath = "/brands"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root                  => Ok(brands.findAll)
    case GET -> Root / BrandIdVar(id) => Ok(brands.findBy(id))
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

object BrandRoutes:
  def make[F[_]: Sync](brands: Brands[F]): F[BrandRoutes[F]] =
    Sync[F].delay(new BrandRoutes[F](brands))
