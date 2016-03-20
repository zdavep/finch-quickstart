package app.models

import io.circe.{ Json, Encoder }

/**
 * Greeting case class.
 */
case class Greeting(message: String)

/**
 * Greeting companion object.
 */
object Greeting {

  /**
   * Provides an implicit JSON encoder for the Greeting model.
   */
  implicit val encodeJson: Encoder[Greeting] =
    Encoder.instance(g => Json.obj("greeting" -> Json.string(g.message)))

  /**
   * Create a new greeting instance.
   */
  def decorate(message: String): Greeting = {
    val sleepMs = 15L
    Thread.sleep(sleepMs) // Simulate some I/O
    Greeting(s"*** $message ***")
  }
}
