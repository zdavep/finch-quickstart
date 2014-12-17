package zdavep

import io.finch.{ Endpoint, HttpRequest, HttpResponse }
import io.finch.json.Json
import io.finch.json.finch._

import com.twitter.finagle.httpx.Status
import com.twitter.finagle.httpx.path._
import com.twitter.finagle.Service
import com.twitter.util.Future

trait NotFoundResponder extends Responder {

  def notFound = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest) = Future.value(
      respondWith(Status.NotFound)(
        Json.obj("status" -> "error", "data" -> "Endpoint not found.")
      )
    )
  }

  val NotFound = new Endpoint[HttpRequest, HttpResponse] {
    def route = { case _ => notFound }
  }

}
