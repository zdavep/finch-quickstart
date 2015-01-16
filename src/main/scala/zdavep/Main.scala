package zdavep

import com.twitter.finagle.Httpx
import com.twitter.util.Await
import zdavep.quickstart._

object Main extends App {

  val version = "v1"
  val backend = quickstartEndpoints(version) orElse endpointNotFound
  val port = if (args.length > 0) args(0).toInt else 8080
  val server = Httpx.serve(new java.net.InetSocketAddress(port), backend.toService)
  Await.ready(server)

}
