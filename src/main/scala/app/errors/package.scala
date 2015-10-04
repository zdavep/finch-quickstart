package app

import argonaut._, Argonaut._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.httpx.{Request, Response}
import com.twitter.util.Future
import io.finch._
import io.finch.argonaut._
import io.finch.response._
import io.finch.request._
import org.slf4j.LoggerFactory

/**
 * Error handling utilities.
 */
package object errors {

  // Logging
  private val log = LoggerFactory.getLogger(getClass)

  // Handle all errors with case classes and return the correct http status
  private[this] def allExceptions: PartialFunction[Throwable, Response] = {
    case RequestErrors(err) => BadRequest {
      log.error(s"Bad request from client in greeting service", err)
      Json("errors" := err.map(e => e.getMessage))
    }
    case e: IllegalArgumentException => BadRequest {
      log.error(s"Bad request from client in greeting service", e)
      Json("error" := Option(e.getMessage).getOrElse("Bad Request"))
    }
    case t: Throwable => InternalServerError {
      log.error(s"Unexpected exception in greeting service", t)
      Json("error" := Option(t.getMessage).getOrElse("Internal server error"))
    }
  }

  /**
   * Function for building illegal argument exception futures.
   */
  def illegalArgs[A](msg: String): Future[A] = new IllegalArgumentException(msg).toFutureException[A]

  /**
   * A Finagle filter that converts exceptions to http responses.
   */
  final val handleExceptions = new SimpleFilter[Request, Response] {
    def apply(req: Request, service: Service[Request, Response]): Future[Response] = service(req) handle allExceptions
  }
}
