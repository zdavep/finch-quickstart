package zdavep

import argonaut._, Argonaut._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.httpx.Status
import com.twitter.util.Future
import io.finch.{Endpoint => _, _}
import io.finch.request._
import io.finch.response._
import io.finch.route._

package object quickstart {

  // Make sure we can encode argonaut json objects using our response builder
  implicit def encodeArgonaut[A](implicit encode: EncodeJson[A]): EncodeResponse[A] =
    EncodeResponse("application/json")(encode.encode(_).nospaces)

  // A HTTP response helper that adds some useful security headers:
  // https://www.owasp.org/index.php/List_of_useful_HTTP_headers
  private[this] def _respondWith(status: Status)(fn: ResponseBuilder => HttpResponse) = fn(
    ResponseBuilder(status).withHeaders("X-Content-Type-Options" -> "nosniff",
      "Strict-Transport-Security" -> "max-age=631138519; includeSubDomains",
      "X-Frame-Options" -> "deny", "X-XSS-Protection" -> "1; mode=block",
      "Cache-Control" -> "max-age=0, no-cache, no-store"))

  // A HTTP response helper that adds some useful security headers:
  // https://www.owasp.org/index.php/List_of_useful_HTTP_headers
  private[this] def respondWith(status: Status)(fn: ResponseBuilder => HttpResponse) =
    _respondWith(status)(fn).toFuture

  // Greeting reader
  private[this] def readGreeting(name: String) = new Service[HttpRequest, String] {
    def buildGreeting(maybeTitle: Option[String]): String = maybeTitle.fold(s"Hello, $name") {
      title => s"Hello, $title $name"
    }
    def apply(req: HttpRequest): Future[String] = (paramOption("title") ~> buildGreeting)(req)
  }

  // Greeting responder
  private[this] val greetingResponder = new Service[String, HttpResponse] {
    def apply(greeting: String): Future[HttpResponse] = respondWith(Status.Ok) { response =>
      response(Json("status" := "success", "greeting" := greeting))
    }
  }

  // Service combinator
  private[this] def greetingService(name: String) = readGreeting(name) ! greetingResponder

  // Greeting endpoint that takes a name parameter
  private[this] def greetingEp1(version: String) =
    Get / "quickstart" / "api" / `version` / "greeting" / string /> greetingService _

  // Greeting endpoint that uses a default name parameter
  private[this] def greetingEp2(version: String) =
    Get / "quickstart" / "api" / `version` / "greeting" /> greetingService("World")

  // Public greeting endpoint combinator
  def quickstartEndpoints(version: String): Endpoint[HttpRequest, HttpResponse] =
    greetingEp1(version) | greetingEp2(version)

  // Handle errors when a requested route was not found.
  private[this] val handleRouteNotFound: PartialFunction[Throwable, HttpResponse] = {
    case e: RouteNotFound => _respondWith(Status.NotFound) { response =>
      response(Json(
        "status" := "error", "error" := Option(e.getMessage).getOrElse("Route Not Found")))
    }
  }

  // Handle any uncaught exceptions.
  private[this] val handleThrowables: PartialFunction[Throwable, HttpResponse] = {
    case t: Throwable => _respondWith(Status.InternalServerError) { response =>
      response(Json(
        "status" := "error", "error" := Option(t.getMessage).getOrElse("Internal Server Error")))
    }
  }

  // Combine all exception handlers.
  private[this] val allExceptions = handleRouteNotFound orElse handleThrowables

  /**
   * A Finagle filter that converts exceptions to http responses.
   */
  val handleExceptions = new SimpleFilter[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest, service: Service[HttpRequest, HttpResponse]): Future[HttpResponse] =
     service(req).handle(allExceptions)
  }

}
