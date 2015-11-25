package exercises

/**
 * Created by zhoufeng on 15/11/22.
 */
import scala.concurrent.{Future, Await, Promise}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
object TestFuture extends App {

  // Compose futures
  val f1 = Future {Thread.sleep(2000); println("run future 1"); 1}
  val f2 = Future {Thread.sleep(1000); println("run future 2"); 2}
  val f3 = Future {Thread.sleep(3000); println("run future 3"); 3}

  // flatMap
  val f4 = for {
    v1 <- f1;
    v2 <- f2;
    v3 <- f3
  } yield List(v1, v2, v3).sum
  // The same as below.
  // val f5 = f1.flatMap {
  //   v1 => f2.flatMap {
  //     v2 => f3.map {
  //       v3 => v1 + v2 + v3
  //     }
  //   }
  // }
  Await.result(f4, Duration.Inf)
  println(s"f4 value is ${f4.value}")

  val f6 = Future {Thread.sleep(2000); println("run future 6"); 1}
  val f7 = Future {Thread.sleep(1000); println("run future 7"); throw new RuntimeException("future 7 exception here")}
  val f8 = Future {Thread.sleep(3000); println("run future 8"); 3}
  val f9 = (for {
    v6 <- f6;
    v7 <- f7;
    v8 <- f8
  } yield List(v6, v7, v8).sum) recover { case _ => 10}
  f9.onFailure{case e => println(e)}
  Await.result(f9, Duration.Inf)
  println(s"f9 value is ${f9.failed}")
}
