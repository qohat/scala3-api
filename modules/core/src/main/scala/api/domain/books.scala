package api.domain

import java.util.UUID

object books:
    enum Category:
        case DRAMA, COMEDY, TECHNOLOGY, SCIENCE_FICTION
    case class BookId(id: UUID)
    case class Book(id: BookId, name: String, category: Category, stock: Int, author: String)