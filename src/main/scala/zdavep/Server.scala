package zdavep

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{ Http, RichHttp, Status, Method }
import com.twitter.finagle.http.path._
import com.twitter.finagle.Service

import io.finch.{ Endpoint, HttpRequest }

object Server extends App with GreetingService with StatusService {

  override val version = "0.2"

  val backend = Endpoint.join(greetingEndpoints, statusEndpoints) orElse Endpoint.NotFound

  val port = if (args.length > 0) args(0).toInt else 8080

  ServerBuilder()
    .codec(RichHttp[HttpRequest](Http()))
    .bindTo(new java.net.InetSocketAddress(port))
    .name("finch-quickstart")
    .build(backend.toService)

}
