import cats.effect.IOApp
import cats.effect.IO
import api.config.SkunkConfing
import cats.effect.ExitCode
import cats.effect.kernel.Resource
import skunk.Session
import natchez.Trace.Implicits.noop
import api.repos.SkunkBrandRepository
import api.services.BrandService
import api.http.routes.BrandRoutes
import api.http.AppHttp
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global
import com.comcast.ip4s.Literals.port

object MainSkunk extends IOApp:
  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 5432,
      user = "admin",
      database = "skunk_db",
      password = Some("password")
    )

  def run(args: List[String]): IO[ExitCode] = session.use { 
    sess =>
        for {
            repo    <- SkunkBrandRepository.make[IO](sess)
            service <- BrandService.make[IO](repo)
            routes  <- BrandRoutes.make[IO](service)
            httpApp <- AppHttp.make[IO](routes)
            _ = println("Creating server...")
            blazeServer <- BlazeServerBuilder[IO](global)
            .bindHttp(9000, "localhost")
            .withHttpApp(httpApp.app)
            .serve
            .compile
            .drain
            .as(ExitCode.Success)
        } yield blazeServer
    }
