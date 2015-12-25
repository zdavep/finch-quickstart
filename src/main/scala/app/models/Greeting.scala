package app.models

import argonaut._, Argonaut._

/**
 * Greeting case class.
 */
case class Greeting(message: String)

/**
 * Greeting companion object.
 */
object Greeting {

  /**
   * Provides an in implementation of the EncodeJson Typeclass (from Argonaut) for Greeting
   */
  implicit def encodeGreetingAsJson: EncodeJson[Greeting] = EncodeJson { (g: Greeting) =>
    ("greeting" := g.message) ->: jEmptyObject
  }

  /**
   * Create a new greeting instance.
   */
  def decorate(message: String): Greeting = {
    val sleepMs = 15L
    Thread.sleep(sleepMs)
    Greeting(s"*** $message ***")
  }
}
