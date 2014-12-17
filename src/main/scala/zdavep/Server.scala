package zdavep

import com.twitter.finagle.Httpx
import com.twitter.util.Await

import io.finch.Endpoint

object Server extends App with GreetingService with StatusService with NotFoundResponder {

  override val version = "0.2"

  val backend = Endpoint.join(greetingEndpoints, statusEndpoints) orElse NotFound

  val port = if (args.length > 0) args(0).toInt else 8080

  val server = Httpx.serve(new java.net.InetSocketAddress(port), backend.toService)
  Await.ready(server)

}
