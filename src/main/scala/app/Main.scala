package app

import com.twitter.finagle.Httpx
import com.twitter.util.Await
import app.routes.greetingAPI
import app.util.config
import app.errors.handleExceptions

/**
 * Greeting service server.
 */
object Main extends App { // Load port and start server
  val port = config.getInt("application.port")
  val _ = Await.ready(Httpx.serve(s":$port", Backend.api))
}

/**
 * Greeting service API.
 */
object Backend { // Init backend API
  val api = handleExceptions andThen greetingAPI
}
