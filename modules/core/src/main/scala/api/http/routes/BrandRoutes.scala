package api.http.routes

import api.http.pathvars.BrandIdVar

import api.services.Brands
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import api.domain.brands._
import org.http4s.circe.CirceEntityCodec.{ circeEntityDecoder, circeEntityEncoder }
import io.circe.generic.auto._
import org.http4s.server.Router
import cats.effect.kernel.Sync
import api.domain.brands.Brand
import org.http4s.circe.JsonDecoder

final case class BrandRoutes[F[_]: Sync: JsonDecoder] private (brands: Brands[F]) extends Http4sDsl[F]:

  private[routes] val prefixPath = "/brands"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root                  => Ok(brands.findAll)
    case GET -> Root / BrandIdVar(id) => Ok(brands.findBy(id))
    case brand @ POST -> Root =>
      brand.req
        .asJsonDecode[Brand]
        .flatMap(brands.save(_))
        .flatMap(Created(_))
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

object BrandRoutes:
  def make[F[_]: Sync](brands: Brands[F]): F[BrandRoutes[F]] =
    Sync[F].delay(new BrandRoutes[F](brands))
