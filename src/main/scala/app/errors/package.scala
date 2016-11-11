package app

import com.twitter.finagle.{ Service, SimpleFilter }
import com.twitter.finagle.http.{ Request, Response, Status }
import com.twitter.util.Future
import io.circe.Json

/**
 * Error handling utilities.
 */
package object errors {

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
          val data = Json.obj(
            "type" -> Json.string(t.getClass.getSimpleName),
            "error" -> Json.string(Option(t.getMessage).getOrElse("Internal Server Error"))
          )
          val rep = Response(Status.InternalServerError)
          rep.setContentTypeJson()
          rep.write(data.noSpaces)
          rep
      }
  }
}
