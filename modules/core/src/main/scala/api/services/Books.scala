package api.services

import api.domain.books._
import api.repos.BookInMemoryRepository
import zio.Task

trait Books[F[_]]:
    def findAll: F[List[Book]]
    def findBy(id: BookId): F[Option[Book]]
    def save(book: Book): F[Unit]
    def update(id: BookId, book: Book): F[Unit]
    def delete(id: BookId): F[Unit]

final case class BookService[F[_]](val repository: BookInMemoryRepository) extends Books[F]:
    def findAll: F[List[Book]] = repository.findAll
    def findBy(id: BookId): F[Option[Book]] = ???
    def save(book: Book): F[Unit] = ???
    def update(id: BookId, book: Book): F[Unit] = ???
    def delete(id: BookId): F[Unit] = ???
    