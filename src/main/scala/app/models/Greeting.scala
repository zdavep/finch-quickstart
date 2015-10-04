package app.models

/**
 * Greeting case class.
 */
case class Greeting(message: String)

/**
 * Greeting companion object.
 */
object Greeting {

  /**
   * Create a new greeting instance.
   */
  def decorate(message: String): Greeting = {
    val sleepMs = 50L
    Thread.sleep(sleepMs)
    Greeting(s"*** $message ***")
  }
}
