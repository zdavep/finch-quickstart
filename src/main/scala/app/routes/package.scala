package app

import app.services._
import io.finch.argonaut._
import io.finch.request._
import io.finch.route._

/**
 * Greeting service routes.
 */
package object routes {

  // Base path
  private val ok = "ok"
  private val base = "zdavep"
  private val path = base / "api" / "v1" / "greeting"

  // Route for rendering multiple greetings
  private val multiGreetingEp = get(path / string / int ? paramOption("title"))(greetings _)

  // Route for rendering a single greeting with a provided name
  private val greetingByNameEp = get(path / string ? paramOption("title"))(greeting _)

  // Route for rendering a single, default greeting
  private val greetingEp = get(path)(greeting("World", None))

  // Route for system status
  private val statusEp = get(base)(ok) | head(base)(ok)

  /**
   * Greeting API
   */
  val greetingAPI = (multiGreetingEp :+: greetingEp :+: greetingByNameEp :+: statusEp).toService
}
