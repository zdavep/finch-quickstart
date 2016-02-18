package app

import argonaut._, Argonaut._
import com.twitter.finagle.{ Service, SimpleFilter }
import com.twitter.finagle.http.{ Request, Response, Status }
import com.twitter.util.Future

/**
 * Error handling utilities.
 */
package object errors {

  // Convert domain errors to Json
  implicit val encodeException: EncodeJson[Exception] = EncodeJson {
    case e: Throwable => Json.obj("error" -> jString(e.getMessage))
  }

  /**
   * Function for building illegal argument exception futures.
   */
  def illegalArgs[A](msg: String): Future[A] = Future.exception(new IllegalArgumentException(msg))

  /**
   * A Finagle filter that converts exceptions to http responses.
   */
  final val errorFilter = new SimpleFilter[Request, Response] {
    def apply(req: Request, service: Service[Request, Response]): Future[Response] =
      service(req).handle {
        case (t: Throwable) =>
          val data = Json("error" := Option(t.getMessage).getOrElse("Internal server error"))
          val rep = Response(Status.InternalServerError)
          rep.setContentTypeJson()
          rep.write(data.nospaces)
          rep
      }
  }
}
