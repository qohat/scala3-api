package api.http

import java.util.UUID
import api.domain.brands.BrandId

import cats.implicits._

object pathvars:
  protected class UUIDVar[A](f: UUID => A):
    def unapply(str: String): Option[A] =
      Either.catchNonFatal(f(UUID.fromString(str))).toOption

  object BrandIdVar extends UUIDVar(BrandId.apply)
