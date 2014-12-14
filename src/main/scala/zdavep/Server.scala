package zdavep

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{ Http, RichHttp, Status, Method }
import com.twitter.finagle.http.path._
import com.twitter.finagle.Service
import com.twitter.util.Future

import io.finch.{ Endpoint, HttpRequest, HttpResponse }
import io.finch.json.Json
import io.finch.json.finch._

object Server extends App with GreetingService with StatusService {

  override val version = "0.2"

  def notFound = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = Future.value(
      respondWith(Status.NotFound)(
        Json.obj("status" -> "error", "data" -> "Endpoint not found.")
      )
    )
  }

  val NotFound = new Endpoint[HttpRequest, HttpResponse] {
    def route = { case _ => notFound }
  }

  val backend = Endpoint.join(greetingEndpoints, statusEndpoints) orElse NotFound

  val port = if (args.length > 0) args(0).toInt else 8080

  ServerBuilder()
    .codec(RichHttp[HttpRequest](Http()))
    .bindTo(new java.net.InetSocketAddress(port))
    .name("finch-quickstart")
    .build(backend.toService)

}
