package api.http.routes

import api.http.pathvars.BrandIdVar

import api.services.Brands
import api.domain.brands._

import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.circe.JsonDecoder

import cats.effect.kernel.Sync
import cats.syntax.all._

import org.http4s.circe._
import org.http4s.circe.CirceEntityCodec.{ circeEntityDecoder, circeEntityEncoder }
import io.circe.generic.auto._

final case class BrandRoutes[F[_]: JsonDecoder: Sync] private (brandService: Brands[F]) extends Http4sDsl[F]:

  private[routes] val prefixPath = "/brands"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root                  => Ok(brandService.findAll)
    case GET -> Root / BrandIdVar(id) => Ok(brandService.findBy(id))
    case brand @ POST -> Root =>
      brand
        .asJsonDecode[Brand]
        .flatMap(brandService.save(_))
        .flatMap(Created(_))
    case brand @ PUT -> Root / BrandIdVar(id) =>
      brand
        .asJsonDecode[Brand]
        .flatMap(brandService.update(id, _))
        .flatMap(Ok(_))
    case DELETE -> Root / BrandIdVar(id) =>
      Ok(brandService.delete(id))
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

object BrandRoutes:
  def make[F[_]: Sync: JsonDecoder](brands: Brands[F]): F[BrandRoutes[F]] =
    Sync[F].delay(new BrandRoutes[F](brands))
