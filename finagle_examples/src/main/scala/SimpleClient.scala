/**
 * Created by zhoufeng on 15/12/5.
 */

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http
import com.twitter.util.{Await, Future}


object SimpleClient extends App {
  val client: Service[http.Request, http.Response] = Http.newService("www.scala-lang.org:80")
  val request = http.Request(http.Method.Get, "/")
  request.host = "www.scala-lang.org"
  val response: Future[http.Response] = client(request)
  response.onSuccess {
    case resp: http.Response => println("GET success: " + resp)
  }
  Await.ready(response)
}
