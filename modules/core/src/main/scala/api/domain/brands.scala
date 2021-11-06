package api.domain

import java.util.UUID
import io.circe.{ Decoder, Encoder, Json }

object brands:
  case class BrandId(value: UUID = UUID.randomUUID)
  case class Brand(id: BrandId, name: String, category: String, stock: Int, author: String)
  enum Category:
    case DRAMA, COMEDY, TECHNOLOGY, SCIENCE_FICTION
  end Category
  enum BrandError(val message: String):
    case FindAllException extends BrandError("Can't find all the records")
    case FindByException extends BrandError("Can't find brand by Id")
    case SaveException extends BrandError("Can't save the brand")
    case UpdateException extends BrandError("Can't update brand")
    case DeleteException extends BrandError("Can't delete the brand")
  end BrandError

  given brandIdDecoder: Decoder[BrandId] = Decoder.instance[BrandId] { cursor =>
    for {
      value <- cursor.downField("value").as[UUID]
    } yield BrandId(value)
  }

  given brandDecoder: Decoder[Brand] = Decoder.instance[Brand] { cursor =>
    for {
      id       <- cursor.downField("id").as[BrandId]
      name     <- cursor.downField("name").as[String]
      category <- cursor.downField("category").as[String]
      stock    <- cursor.downField("stock").as[Int]
      author   <- cursor.downField("author").as[String]
    } yield Brand(id, name, category, stock, author)
  }

end brands
