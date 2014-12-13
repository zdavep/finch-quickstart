package zdavep

import io.finch.{ Endpoint, HttpRequest, HttpResponse }
import io.finch.json.Json
import io.finch.json.finch._
import io.finch.request.OptionalParam
import io.finch.response.Respond

import com.twitter.finagle.http.{ Status, Method }
import com.twitter.finagle.http.path._
import com.twitter.finagle.Service

trait HelloService {

  val version = "0.2"

  val okResponse = Respond(Status.Ok).withHeaders(
    "X-Content-Type-Options" -> "nosniff", "X-Frame-Options" -> "deny",
    "X-XSS-Protection" -> "1; mode=block",
    "Strict-Transport-Security" -> "max-age=16070400; includeSubDomains"
  )

  def hello(name: String) = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = for {
      title <- OptionalParam("title")(req)
    } yield okResponse(
      Json.obj("status" -> "success", "data" -> s"Hello, ${title.getOrElse("")}$name!")
    )
  }

  val helloEndpoints = new Endpoint[HttpRequest, HttpResponse] {
    def route = {
      case Method.Get -> Root / "hello" / "api" / `version` / "greeting" / name => hello(name)
      case Method.Get -> Root / "hello" / "api" / `version` / "greeting" => hello("World")
    }
  }

}
