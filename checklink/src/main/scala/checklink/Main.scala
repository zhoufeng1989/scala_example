package checklink

/**
 * Created by zhoufeng on 15/12/13.
 */
import akka.actor.{Actor, Props}


class Main extends Actor {
  import Receptionist._

  val receptionist = context.actorOf(Props[Receptionist], "receptionist")

  receptionist ! Get("http://google.com")
  receptionist ! Get("http://google.com/1")
  receptionist ! Get("http://google.com/2")
  receptionist ! Get("http://google.com/3")
  receptionist ! Get("http://google.com/4")
  receptionist ! Get("http://google.com/5")

  def receive: Receive = {
    case Result(url, urls) =>
      println(urls.toVector.sorted.mkString(s"Result for '$url':\n", "\n", "\n"))
    case Failed(url, reason) =>
      println(s"'$url' failed because of $reason\n")
  }
}
