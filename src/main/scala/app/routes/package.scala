package app

import app.errors._
import app.models.Greeting
import app.services._
import io.finch._
import io.finch.argonaut._

/**
 * Greeting service routes.
 */
package object routes {

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

  /**
   * Greeting API
   */
  val greetingAPI = endpoints.handle(domainExceptions).toService
}
