package checklink

/**
 * Created by zhoufeng on 15/12/12.
 */
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.Timeout
import scala.collection.mutable
import scala.concurrent.duration._


class Controller extends Actor with ActorLogging {
  implicit val exec = context.dispatcher
  val cache = mutable.Set.empty[String]
  val children = mutable.Set.empty[ActorRef]
  context.system.scheduler.scheduleOnce(100 second, self, Timeout)

  def receive = {
    case Controller.Check(url, depth) => {
      if (!cache(url) && depth > 0) {
        children += context.actorOf(Props(new Getter(url, depth - 1)))
      }
      cache += url
    }
    case Getter.Done => {
      children -= sender
      if (children.isEmpty) context.parent ! Controller.Result(cache)
    }
    case Timeout => children.foreach (_ ! Getter.Abort)
  }


}

object Controller {
  case class Check(url: String, depth: Int)
  case class Result(urls: mutable.Set[String])
}
