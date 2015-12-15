package checklink

/**
 * Created by zhoufeng on 15/12/12.
 */
import akka.actor.{Actor, ActorRef, Props}
import scala.collection.mutable


class Receptionist extends Actor {
  import Receptionist._
  var reqNo = 0
  def receive = waitting

  def waitting: Receive = {
    case Get(url) => context.become(runNext(Vector(Job(sender, url))))
  }

  def running(queue: Vector[Job]): Receive = {
    case Get(url) => context.become(enqueueJob(queue, Job(sender, url)))
    case Controller.Result(urls) => {
      val job = queue.head
      job.client ! Result(job.url, urls)
      context.stop(sender)
      context.become(runNext(queue.tail))
    }
  }

  def runNext(queue: Vector[Job]): Receive = {
    if (queue.isEmpty) waitting
    else {
      reqNo += 1
      val controller = context.actorOf(Props[Controller], s"c$reqNo")
      controller ! Controller.Check(queue.head.url, 2)
      running(queue)
    }
  }

  def enqueueJob(queue: Vector[Job], job: Job): Receive = {
    if (queue.size > 3) {
      sender ! Failed(job.url, "too many jobs in queue.")
      running(queue)
    }
    else running(queue :+ job)
  }

}


object Receptionist {
  private case class Job(client: ActorRef, url: String)
  case class Get(url: String)
  case class Result(val url: String, val urls: mutable.Set[String])
  case class Failed(url: String, reason: String)
}
