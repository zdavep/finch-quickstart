package zdavep

import io.finch.response.ResponseBuilder
import org.jboss.netty.handler.codec.http.HttpResponseStatus

trait Responder {

  def respondWith(status: HttpResponseStatus) = ResponseBuilder(status).withHeaders(
    "X-Content-Type-Options" -> "nosniff", "X-Frame-Options" -> "deny",
    "X-XSS-Protection" -> "1; mode=block",
    "Strict-Transport-Security" -> "max-age=16070400; includeSubDomains"
  )

}
