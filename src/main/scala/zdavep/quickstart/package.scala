package zdavep

import com.twitter.finagle.Service
import com.twitter.finagle.httpx.path._
import com.twitter.finagle.httpx.{Method, Status}
import com.twitter.util.Await
import com.twitter.util.Future
import io.finch._
import io.finch.json._
import io.finch.request.OptionalParam
import io.finch.response._

package object quickstart {

  /**
   * A HTTP response helper that adds some useful security headers:
   * https://www.owasp.org/index.php/List_of_useful_HTTP_headers
   */
  def respondWith(status: Status)(fn: ResponseBuilder => HttpResponse): Future[HttpResponse] =
  fn(ResponseBuilder(status).withHeaders(
    "Strict-Transport-Security" -> "max-age=631138519; includeSubDomains",
    "X-Content-Type-Options" -> "nosniff",
    "X-Frame-Options" -> "deny",
    "X-XSS-Protection" -> "1; mode=block",
    "Cache-Control" -> "max-age=0, no-cache, no-store")
  ).toFuture

  /**
   * Service for rendering a simple greeting.
   */
  def greetingService(name: String): Service[HttpRequest, HttpResponse] =
      new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest): Future[HttpResponse] = respondWith(Status.Ok) { response =>
      val json = for { title <- OptionalParam("title")(req) } yield title match {
        case Some(title) => Json.obj("status" -> "success", "data" -> s"Hello, $title $name!")
        case None => Json.obj("status" -> "success", "data" -> s"Hello, $name!")
      }
      response(Await.result(json))
    }
  }

  /**
   * Service for getting system status.
   */
  val statusService = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest): Future[HttpResponse] = respondWith(Status.Ok) { response =>
      val msg = for { msg <- OptionalParam("msg")(req) } yield msg.getOrElse("ok")
      response(Json.obj("status" -> "success", "data" -> s"${Await.result(msg)}"))
    }
  }

  /**
   * Endpoints for rendering a simple greeting and getting system status.
   */
  def quickstartEndpoints(version: String): Endpoint[HttpRequest, HttpResponse] =
      new Endpoint[HttpRequest, HttpResponse] {

    private[this] val uriBase = Root / "quickstart" / "api" / `version`

    override def route = {
      case  Method.Get -> `uriBase` / "greeting" / name => greetingService(name)
      case  Method.Get -> `uriBase` / "greeting" => greetingService("World")
      case  Method.Get -> `uriBase` / "status" => statusService
      case Method.Head -> `uriBase` / "status" => statusService
    }

  }

  /**
   * A helper endpoint that renders a json message with a 404 status.
   */
  val endpointNotFound = new Endpoint[HttpRequest, HttpResponse] {
    override def route = {
      case _ => new Service[HttpRequest, HttpResponse] {
        def apply(req: HttpRequest): Future[HttpResponse] = respondWith(Status.NotFound) {
          response => response(Json.obj("status" -> "error",  "error" -> "Endpoint not found"))
        }
      }
    }
  }

}
