import cats.effect.IOApp
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.kernel.Ref
import api.domain.brands._
import api.repos.BrandRepository
import api.repos.BrandInMemoryRepository
import api.services.BrandService
import api.http.routes.BrandRoutes
import api.http.AppHttp
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global
import java.util.UUID

object Main extends IOApp:
  val id0 = BrandId(UUID.randomUUID)
  val id1 = BrandId(UUID.randomUUID)
  val inMemoryMap = Map[String, Brand](
    id0.value.toString -> Brand(id0, "New Brand", Category.COMEDY.toString, 12, "Jez"),
    id1.value.toString -> Brand(
      id1,
      "Another Brand with information from book",
      Category.SCIENCE_FICTION.toString,
      15,
      "Alejandro"
    )
  )
  def run(args: List[String]): IO[ExitCode] =
    for {
      inMemoryDB <- Ref[IO].of(inMemoryMap)
      repo       <- BrandInMemoryRepository.make[IO](inMemoryDB)
      service    <- BrandService.make[IO](repo)
      routes     <- BrandRoutes.make[IO](service)
      httpApp    <- AppHttp.make[IO](routes)
      _ = println("Creating server...")
      blazeServer <- BlazeServerBuilder[IO](global)
        .bindHttp(9000, "localhost")
        .withHttpApp(httpApp.app)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    } yield blazeServer

/*@main def hello: Unit =
  println("Hello world!")
  println(msg)

def msg = "I was compiled by Scala 3. :)"*/
