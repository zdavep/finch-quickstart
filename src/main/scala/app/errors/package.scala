package app

import argonaut._, Argonaut._
import com.twitter.util.Future
import io.finch._
import org.slf4j.LoggerFactory

/**
 * Error handling utilities.
 */
package object errors {

  // Logging
  private val log = LoggerFactory.getLogger(getClass)

  // Convert domain errors to Json
  implicit val encodeException: EncodeJson[Exception] = EncodeJson {
    case e: Throwable => Json.obj("error" -> jString(e.getMessage))
  }

  // Handle all errors and return the correct http status
  def allExceptions: PartialFunction[Throwable, Output.Failure] = {
    case e: IllegalArgumentException =>
      log.error(s"Bad request from client", e)
      BadRequest(e)
    case t: Throwable =>
      log.error(s"Unexpected exception", t)
      InternalServerError(new Exception(t.getCause))
  }


  /**
   * Function for building illegal argument exception futures.
   */
  def illegalArgs[A](msg: String): Future[A] = Future.exception(new IllegalArgumentException(msg))
}
