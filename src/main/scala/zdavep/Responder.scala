package zdavep

import io.finch.response.ResponseBuilder
import com.twitter.finagle.httpx.Status

trait Responder {

  def respondWith(status: Status) = ResponseBuilder(status).withHeaders(
    "X-Content-Type-Options" -> "nosniff", "X-Frame-Options" -> "deny",
    "X-XSS-Protection" -> "1; mode=block",
    "Strict-Transport-Security" -> "max-age=16070400; includeSubDomains"
  )

}
