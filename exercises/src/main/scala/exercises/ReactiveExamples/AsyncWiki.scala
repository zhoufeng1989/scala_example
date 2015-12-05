package exercises.ReactiveExamples

/**
 * Created by zhoufeng on 15/12/1.
 */

import rx.lang.scala.Observable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.Scanner
import java.net.URL

object AsyncWiki extends App {
  def fetchWikiArticles(wikiArticleNames: List[String]): List[Future[String]] =
      wikiArticleNames.map(
        articleName => Future {
          val url = "https://en.wikipedia.org/wiki/" + articleName
          val art = new Scanner(new URL(url).openStream()).useDelimiter("\\A").next()
          art
        }
      )

  val futures = fetchWikiArticles(List("hello", "world", "test"))
  val obs = futures.foldRight(Observable[String](observer => observer.onCompleted()))(
    (x, z) => z.mergeDelayError(Observable.from(x)))
  obs.subscribe(println(_))
  readLine()
}
