package app

import argonaut._, Argonaut._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response, Status}
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

  // Error reply helper
  private def reply(status: Status, data: String) = {
    val rep = Response(status)
    rep.setContentTypeJson()
    rep.write(data)
    rep
  }

  /**
   * A Finagle filter that converts exceptions to http responses.
   */
  final val exceptionFilter = new SimpleFilter[Request, Response] {
    def apply(req: Request, service: Service[Request, Response]): Future[Response] =
      service(req).handle { case (t: Throwable) =>
        val data = Json("error" := Option(t.getMessage).getOrElse("Internal server error"))
        reply(Status.InternalServerError, data.toString())
      }
  }
}
