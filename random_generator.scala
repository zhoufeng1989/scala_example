trait Generator[+T] {
  self => 

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    def generate = f(self.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate = f(self.generate).generate
  }
}

val integers = new Generator[Int] {
  val rand = new java.util.Random
  def generate = rand.nextInt
}

val booleans = integers map (_ > 0)
val booleans2 = for(x <- integers) yield x > 0

val pairs = integers flatMap {x => integers map { y => (x, y)}}
val pairs2 = for {x <- integers; y <- integers} yield (x, y)
