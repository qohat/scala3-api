package api.services

import api.domain.books._
import api.repos.BookInMemoryRepository
import zio.IO
import java.io.IOException

trait Books:
  def findAll: IO[IOException, List[Book]]
  def findBy(id: BookId): IO[IOException, Option[Book]]
  def save(book: Book): IO[IOException, Unit]
  def update(id: BookId, book: Book): IO[IOException, Unit]
  def delete(id: BookId): IO[IOException, Unit]

object BookService:
  def make(repository: BookInMemoryRepository) =
    new Books {
      def findAll: IO[IOException, List[Book]]                  = 
          repository.findAll
          .mapError {
              case e: IOException => e
              case _ => IOException("Failed finding all")
          }
      def findBy(id: BookId): IO[IOException, Option[Book]]     = 
          repository.findBy(id)
          .mapError {
              case e: IOException => e
              case _ => IOException("Failed finding by Id")
          }
      def save(book: Book): IO[IOException, Unit]               = 
          repository.save(book)
          .mapError {
              case e: IOException => e
              case _ => IOException("Failed saving")
          }
      def update(id: BookId, book: Book): IO[IOException, Unit] = 
          repository.update(id, book)
          .mapError {
              case e: IOException => e
              case _ => IOException("Failed update")
          }
      def delete(id: BookId): IO[IOException, Unit]             = 
          repository.delete(id)
          .mapError {
              case e: IOException => e
              case _ => IOException("Failed deleting")
          }

    }
