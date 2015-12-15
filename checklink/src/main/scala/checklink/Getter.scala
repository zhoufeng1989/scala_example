package checklink

/**
 * Created by zhoufeng on 15/12/12.
 */
import akka.actor.Actor
import akka.actor.Status
import akka.pattern.pipe
import org.jsoup.Jsoup
import scala.collection.JavaConverters._


class Getter(url: String, depth: Int) extends Actor {
  implicit val exec = context.dispatcher
  Client(url)(exec) pipeTo self

  def receive = {
    case body: String => Getter.findLinks(body).foreach {
      context.parent ! Controller.Check(_, depth)
    }
      stop()
    case _: Status.Failure => stop()
  }

  def stop() = {
    context.parent ! Getter.Done
    context.stop(self)
  }

}


object Getter {
  object Done
  object Abort

  def findLinks(body: String): Iterator[String] = {
    val document = Jsoup.parse(body)
    val links = document.select("a[href]")
    links.iterator().asScala map (_.absUrl("href"))
  }
}
