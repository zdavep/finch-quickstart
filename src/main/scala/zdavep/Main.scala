package zdavep

import com.twitter.finagle.Httpx
import com.twitter.util.Await
import zdavep.quickstart._

object Main extends App {

  val version = "v1"
  val backend = quickstartEndpoints(version) orElse endpointNotFound

  val defaultPort = 8080
  val port = if (args.length > 0) args(0).toInt else defaultPort

  val server = Httpx.serve(new java.net.InetSocketAddress(port), backend)
  val _ = Await.ready(server)

}
