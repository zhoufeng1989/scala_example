package checklink

/**
 * Created by zhoufeng on 15/12/9.
 */
import dispatch.{Http, url, as}
import scala.concurrent.{Future, ExecutionContext}
object Client {
  def apply(urlStr: String)(implicit ec: ExecutionContext): Future[String] = {
    val svc = url(urlStr)
    Http(svc OK as.String)
  }
}
