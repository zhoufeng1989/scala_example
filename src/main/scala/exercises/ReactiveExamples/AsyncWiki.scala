package exercises.ReactiveExamples

/**
 * Created by zhoufeng on 15/12/1.
 */

import rx.lang.scala.Observable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import java.util.Scanner
import java.net.URL

object AsyncWiki extends App {
  def fetchWikiArticles(wikiArticleNames: List[String]): Future[List[String]] =
    Future.sequence(
      wikiArticleNames.map(
        articleName => Future {
          val url = "https://en.wikipedia.org/wiki/" + articleName
          val art = new Scanner(new URL(url).openStream()).useDelimiter("\\A").next()
          art
        }
      )
    )

  val future = fetchWikiArticles(List("hello"))
  val observable = Observable.from(future)
  observable.subscribe(println(_))
  readLine()
}
