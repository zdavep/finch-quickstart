package zdavep

import io.finch._
import io.finch.json._
import io.finch.request._
import io.finch.response._

import java.net.InetSocketAddress

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Http, RichHttp, Status, Method}
import com.twitter.finagle.http.path._
import com.twitter.finagle.Service

object Hello extends App {

  val okResponse = Respond(Status.Ok).withHeaders(
    "X-Content-Type-Options" -> "nosniff", "X-Frame-Options" -> "deny",
    "X-XSS-Protection" -> "1; mode=block",
    "Strict-Transport-Security" -> "max-age=16070400; includeSubDomains"
  )

  def hello(name: String) = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = for {
      title <- OptionalParam("title")(req)
    } yield {
      okResponse(
        JsonObject("status" -> "success", "data" -> s"Hello, ${title.getOrElse("")}$name!"))
    }
  }

  val helloEndpoints = new Endpoint[HttpRequest, HttpResponse] {
    def route = {
      case Method.Get -> Root / "hello" / version / "greeting" / name => hello(name)
      case Method.Get -> Root / "hello" / version / "greeting" => hello("World")
    }
  }

  val backend = helloEndpoints orElse Endpoint.NotFound

  val port = if (args.length > 0) args(0).toInt else 8080

  ServerBuilder().codec(RichHttp[HttpRequest](Http())).bindTo(new InetSocketAddress(port))
    .name("finch-quickstart").build(backend.toService)

} // Hello
