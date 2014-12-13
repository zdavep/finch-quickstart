package zdavep

import io.finch.{ Endpoint, HttpRequest, HttpResponse }
import io.finch.json.Json
import io.finch.json.finch._
import io.finch.request.OptionalParam

import com.twitter.finagle.http.{ Method, Status }
import com.twitter.finagle.http.path._
import com.twitter.finagle.Service

trait GreetingService extends Versioned with Responder {

  def greet(name: String) = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = for {
      title <- OptionalParam("title")(req)
    } yield respondWith(Status.Ok)(
      Json.obj("status" -> "success", "data" -> s"Hello, ${title.getOrElse("")}$name!")
    )
  }

  val greetingEndpoints = new Endpoint[HttpRequest, HttpResponse] {
    def route = {
      case Method.Get -> Root / "z" / "api" / `version` / "greeting" / name => greet(name)
      case Method.Get -> Root / "z" / "api" / `version` / "greeting" => greet("World")
    }
  }

}
