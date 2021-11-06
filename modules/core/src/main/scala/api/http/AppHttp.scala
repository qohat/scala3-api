package api.http

import org.http4s.server.middleware.RequestLogger
import org.http4s.server.middleware.ResponseLogger
import api.http.routes.BrandRoutes
import cats.effect.kernel.Sync
import concurrent.duration.DurationInt
import org.http4s.HttpRoutes
import org.http4s.server.middleware.AutoSlash
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.Timeout
import org.http4s.HttpApp
import cats.effect.kernel.Async

sealed abstract class AppHttp[F[_]: Async] private (brandsRoutes: BrandRoutes[F]):

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    { (http: HttpRoutes[F]) =>
      AutoSlash(http)
    } andThen { (http: HttpRoutes[F]) =>
      Timeout(60.seconds)(http)
    }
  }

  private val loggers: HttpApp[F] => HttpApp[F] = {
    { (http: HttpApp[F]) =>
      RequestLogger.httpApp(true, true)(http)
    } andThen { (http: HttpApp[F]) =>
      ResponseLogger.httpApp(true, true)(http)
    }
  }

  val app: HttpApp[F] = loggers(middleware(brandsRoutes.routes).orNotFound)

object AppHttp:
  def make[F[_]: Async](brandsRoutes: BrandRoutes[F]): F[AppHttp[F]] =
    Async[F].delay(new AppHttp[F](brandsRoutes) {})
