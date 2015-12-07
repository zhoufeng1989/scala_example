/**
 * Created by zhoufeng on 15/12/7.
 */

import akka.actor.{Actor, Props}

class Counter extends Actor {
  def counter(i: Int): Receive = {
    case "incr" => context.become(counter(i + 1))
    case "get" => {sender ! i }
  }

  def receive = counter(0)
}

class Customer extends Actor {
  val counter =  context.actorOf(Props[Counter], "counter")

  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "get"

  def receive = {
    case count: Int => {
      println(s"count was $count")
      context.stop(self)
    }
  }
}
