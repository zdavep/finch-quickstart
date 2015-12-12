package app

import com.twitter.conversions.time._
import com.twitter.finagle.Http
import com.twitter.util.Await
import app.routes.greetingAPI
import app.util.{config, timeoutFilter}

/**
 * Greeting service server.
 */
object Main extends App { // Load port and start server
  val port = config.getInt("app.port")
  val _ = Await.ready(Http.serve(s":$port", Backend.api))
}

/**
 * Greeting service API.
 */
object Backend { // Init backend API
  val api = timeoutFilter(250.millis) andThen greetingAPI
}
