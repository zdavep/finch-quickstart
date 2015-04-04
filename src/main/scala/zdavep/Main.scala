package zdavep

import com.twitter.finagle.Httpx
import com.twitter.util.Await
import io.finch.{Endpoint => _, _}
import zdavep.quickstart._

object Main extends App {

  val version = "v1"
  val backend = handleExceptions ! quickstartEndpoints(version)

  val server = Httpx.serve(":8080", backend)
  val _ = Await.ready(server)

}
