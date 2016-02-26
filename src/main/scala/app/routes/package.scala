package app

//import app.errors._
import app.models.Greeting
import app.services._
import io.circe.{ Json, Encoder }
import io.finch._
import io.finch.circe._

/**
 * Greeting service routes.
 */
package object routes {

  // Logging
  private val log = org.slf4j.LoggerFactory.getLogger(getClass)

  // Base path
  private val health = "ok"
  private val base = "zdavep"
  private val path = base / "api" / "v1" / "greeting"

  // Route for rendering multiple greetings
  private val multiGreetingEp = get(path / string / int ? paramOption("title")) {
    (name: String, count: Int, title: Option[String]) => Ok(greetings(name, count, title))
  }

  // Route for rendering a single greeting with a provided name
  private val greetingByNameEp: Endpoint[Greeting] = get(path / string ? paramOption("title")) {
    (name: String, title: Option[String]) => Ok(greeting(name, title))
  }

  // Route for rendering a single, default greeting
  private val greetingEp: Endpoint[Greeting] = get(path)(Ok(greeting("World", None)))

  // Route for system status
  private val statusEp = get(base)(Ok(health)) | head(base)(Ok(health))

  // Endpoints
  private val endpoints = multiGreetingEp :+: greetingEp :+: greetingByNameEp :+: statusEp

  // Convert domain errors to JSON
  implicit val encodeException: Encoder[Exception] = Encoder.instance(e =>
    Json.obj(
      "type" -> Json.string(e.getClass.getSimpleName),
      "error" -> Json.string(Option(e.getMessage).getOrElse("Internal Server Error"))
    ))

  /**
   * Greeting API
   */
  val greetingAPI = endpoints.handle {
    case e: IllegalArgumentException =>
      log.error("Bad request from client", e)
      BadRequest(e)
    case t: Throwable =>
      log.error("Unexpected exception", t)
      InternalServerError(new Exception(t.getCause))
  } toService
}
