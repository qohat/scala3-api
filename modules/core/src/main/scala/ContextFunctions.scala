import scala.concurrent.ExecutionContext
import scala.collection.mutable.ArrayBuffer

type Executable[T] = ExecutionContext ?=> T

given ec: ExecutionContext = ExecutionContext.global

def f(x: Int): ExecutionContext ?=> Int = x * 1

def g(arg: Executable[Int]) = arg * arg

// could be written as follows with the type alias from above
// def f(x: Int): Executable[Int] = ...

class Table:
  val rows              = new ArrayBuffer[Row]
  def add(r: Row): Unit = rows += r
  override def toString = rows.mkString("Table(", ", ", ")")

class Row:
  val cells              = new ArrayBuffer[Cell]
  def add(c: Cell): Unit = cells += c
  override def toString  = cells.mkString("Row(", ", ", ")")

case class Cell(elem: String)

def table(init: Table ?=> Unit) =
  given t: Table = Table()
  init
  t

def row(init: Row ?=> Unit)(using t: Table) =
  given r: Row = Row()
  init
  t.add(r)

def cell(str: String)(using r: Row) =
  r.add(new Cell(str))

val theTable = table { ($t: Table) ?=>
  row { ($r: Row) ?=>
    cell("top left")(using $r)
    cell("top right")(using $r)
  }(using $t)

  row { ($r: Row) ?=>
    cell("bottom left")(using $r)
    cell("bottom right")(using $r)
  }(using $t)
}

object PostConditions:
  opaque type WrappedResult[T] = T

  def result[T](using r: WrappedResult[T]): T = r

  extension [T](x: T)
    def ensuring(condition: WrappedResult[T] ?=> Boolean): T =
      assert(condition(using x))
      x
end PostConditions
import PostConditions.{ ensuring, result }

val s = List(1, 2, 3).sum.ensuring(result == 6)

@main def hello: Unit =
  println(f(2)(using ec)) // explicit argument
  println(f(2))
  println(g(4))
  println(g(f(2)))
  println(g((ctx: ExecutionContext) ?=> f(3)))
  println(theTable)
  println(s)
