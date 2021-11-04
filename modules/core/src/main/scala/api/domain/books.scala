package api.domain

import java.util.UUID

object books:
  case class BookId(id: UUID)
  case class Book(id: BookId, name: String, category: Category, stock: Int, author: String)
  enum Category:
    case DRAMA, COMEDY, TECHNOLOGY, SCIENCE_FICTION
  end Category
  enum BookError(val message: String):
    case FindAllException extends BookError("Can't find all the records")
    case FindByException extends BookError("Can't find book by Id")
    case SaveException extends BookError("Can't save the book")
    case UpdateException extends BookError("Can't update book")
    case DeleteException extends BookError("Can't delete the book")
  end BookError
end books
