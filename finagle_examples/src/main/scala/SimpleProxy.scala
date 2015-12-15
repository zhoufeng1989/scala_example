/**
 * Created by zhoufeng on 15/12/5.
 */
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http
import com.twitter.util.Await

object SimpleProxy extends App {
  val client: Service[http.Request, http.Response] = Http.newService("google.com:80")

  val server = Http.serve(":8080", client)
  Await.ready(server)
}
