package api.repos

import api.domain.books._
import java.util.UUID
import zio.ZIO
import zio.internal.Sync
import zio.Task

trait BookInMemoryRepository:
    def findAll: Task[List[Book]]
    def findBy(id: BookId): Task[Option[Book]]
    def save(book: Book): Task[Unit]
    def update(id: BookId, book: Book): Task[Unit]
    def delete(id: BookId): Task[Unit]

object BookInMemoryRepository:
    private val map: Map[BookId, Book] = Map[BookId, Book]()
    def make: BookInMemoryRepository = 
        new BookInMemoryRepository {
            def findAll: Task[List[Book]] = Task.succeed(map.values.toList)
            def findBy(id: BookId): Task[Option[Book]] = Task.succeed(map.get(id))
            def save(book: Book): Task[Unit] = Task.from(map + (book.id -> book)).map(_ => ())
            def update(id: BookId, book: Book): Task[Unit] = Task.from(map + (id -> book)).map(_ => ())
            def delete(id: BookId): Task[Unit] = Task.from(map - (id)).map(_ => ())
        }