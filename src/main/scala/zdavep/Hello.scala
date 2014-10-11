package zdavep

import io.finch._
import io.finch.json._
import io.finch.request._

import com.twitter.finagle.Service
import com.twitter.finagle.http.Method
import com.twitter.finagle.http.path._
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Http, RichHttp}

import java.net.InetSocketAddress

object Hello extends App {

  def hello(name: String) = new Service[HttpRequest, JsonResponse] {
    def apply(req: HttpRequest) = for {
      title <- OptionalParam("title")(req)
    } yield JsonObject(
      "status" -> "success", "data" -> s"Hello, ${title.getOrElse("")}$name!"
    )
  }

  val endpoint = new Endpoint[HttpRequest, JsonResponse] {
    def route = {
      case Method.Get -> Root / "hello" / version / "greeting" / name => hello(name)
      case Method.Get -> Root / "hello" / version / "greeting"  => hello("World")
    }
  }

  val backend = (endpoint ! TurnJsonIntoHttp) orElse Endpoint.NotFound

  ServerBuilder().codec(RichHttp[HttpRequest](Http())).bindTo(new InetSocketAddress(8080))
    .name("finch-quickstart").build(backend.toService)

} // Hello
