package api.domain

import java.util.UUID

object brands:
  case class BrandId(value: UUID)
  case class Brand(id: BrandId, name: String, category: Category, stock: Int, author: String)
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
end brands
