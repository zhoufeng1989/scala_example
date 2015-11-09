package exercises

import java.util.Random

/**
 * Created by zhoufeng on 15/11/9.
 */
trait Generator[+T] {
  self =>

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    override def generate: S = f(self.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    override def generate: S = f(self.generate).generate
  }
}

object Generator {
  def integers = new Generator[Int] {
    val rand = new Random
    def generate = rand.nextInt
  }

  val booleans = integers map (_ > 0)

  def doubles = new Generator[Double] {
    val rand = new Random
    def generate = rand.nextDouble
  }

  def pairs[T, U](t: Generator[T], u: Generator[U]): Generator[(T, U)] = for (x <- t; y <- u) yield (x, y)

  def single[T](t: T): Generator[T] = new Generator[T] {
    def generate = t
  }

  def choose(lo: Int, hi: Int): Generator[Int] = for (x <- integers) yield lo + scala.math.abs(x) % (hi - lo)

  def oneOf[T](xs: T*): Generator[T] = for(x <- choose(0, xs.length)) yield xs(x)

  def emptyLists = single(Nil)

  def noEmptyLists = for{
    head <- integers;
    tail <- lists
  }yield head::tail

  def lists = for {
    x <- booleans
    list <- if (x) emptyLists else noEmptyLists
  }yield list
}
