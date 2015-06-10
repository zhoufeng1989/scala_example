trait Generator[+T] {
  self => 

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    def generate = f(self.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate = f(self.generate).generate
  }


  def integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt
  }

  def booleans = integers map (_ > 0)
  def booleans2 = for(x <- integers) yield x > 0


  def pairs[T, U](t: Generator[T], u: Generator[U]) = t flatMap {x => u map {y => (x, y)}}
  def pairs2[T, U](t: Generator[T], u: Generator[U]) = for (x <- t; y <-u) yield (x, y)

  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  def choose(lo: Int, hi: Int): Generator[Int] = 
    for (x <- integers) yield lo + scala.math.abs(x) % (hi - lo)

  def oneOf[T](xs: T*): Generator[T] = 
    for (idx <- choose(0, xs.length)) yield xs(scala.math.abs(idx))


  def emptyLists = single(Nil)

  def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head :: tail

  def lists: Generator[List[Int]] = for {
    isEmpty <- booleans
    list <- if (isEmpty) emptyLists else nonEmptyLists
  } yield list
}

trait Tree

case class Inner(left: Tree, right: Tree) extends Tree

case class Leaf(x: Int) extends Tree

val integers = new Generator[Int] {
  val rand = new java.util.Random
  def generate = rand.nextInt
}

val booleans = for (x <- integers) yield x > 0

def leafs: Generator[Leaf] = for (x <- integers) yield Leaf(x)

def trees: Generator[Tree] =
  for {
    isLeaf <- booleans
    tree <- if(isLeaf) leafs else inners
  } yield tree

def inners: Generator[Inner] = for {
  l <- trees
  r <- trees
} yield Inner(l, r)
