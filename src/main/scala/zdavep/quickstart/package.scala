package zdavep

import com.twitter.finagle.Service
import com.twitter.finagle.httpx.path._
import com.twitter.finagle.httpx.{Method, Status}
import com.twitter.util.Future
import io.finch._
import io.finch.json._
//import io.finch.json.finch._
import io.finch.request.OptionalParam
import io.finch.response._

package object quickstart {

  /**
   * A HTTP response helper that adds some useful security headers:
   * https://www.owasp.org/index.php/List_of_useful_HTTP_headers
   */
  def respondWith(status: Status)(f: ResponseBuilder => HttpResponse) = f(
    ResponseBuilder(status).withHeaders(
      "Strict-Transport-Security" -> "max-age=631138519; includeSubDomains",
      "X-Content-Type-Options" -> "nosniff",
      "X-Frame-Options" -> "deny",
      "X-XSS-Protection" -> "1; mode=block",
      "Content-Security-Policy" -> "default-src 'self' http://localhost:8080",
      "Cache-Control" -> "max-age=0, no-cache, no-store"
    )
  ).toFuture

  /**
   * Service for rendering a simple greeting.
   */
  def greetingService(name: String) = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = {
      val titleFuture: Future[String] = for { title <- OptionalParam("title")(req) } yield title match {
        case Some(t) => s"$t "
        case None => ""
      }
      titleFuture flatMap { title =>
        respondWith(Status.Ok) { response =>
          response(Json.obj("status" -> "success", "data" -> s"Hello, $title$name!"))
        }
      }
    }
  }

  /**
   * Service for getting system status.
   */
  def statusService = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = {
      val msgFuture: Future[String] = for { msg <- OptionalParam("msg")(req) } yield msg.getOrElse("ok")
      msgFuture flatMap { msg =>
        respondWith(Status.Ok) { response =>
          response(Json.obj("status" -> "success", "data" -> s"$msg"))
        }
      }
    }
  }

  /**
   * Endpoints for rendering a simple greeting and getting system status.
   */
  def quickstartEndpoints(version: String) = new Endpoint[HttpRequest, HttpResponse] {
    def route = {
      case  Method.Get -> Root / "quickstart" / "api" / `version` / "greeting" / name => greetingService(name)
      case  Method.Get -> Root / "quickstart" / "api" / `version` / "greeting" => greetingService("World")
      case  Method.Get -> Root / "quickstart" / "api" / `version` / "status" => statusService
      case Method.Head -> Root / "quickstart" / "api" / `version` / "status" => statusService
    }
  }

  /**
   * A helper endpoint that renders a json message with a 404 status.
   */
  val endpointNotFound = new Endpoint[HttpRequest, HttpResponse] {
    def route = {
      case _ => new Service[HttpRequest, HttpResponse] {
        def apply(req: HttpRequest) = respondWith(Status.NotFound) { response =>
          response(Json.obj("status" -> "error",  "error" -> "Endpoint not found"))
        }
      }
    }
  }

}
