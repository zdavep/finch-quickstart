package app.tests

import com.twitter.finagle.Service
import com.twitter.finagle.http.{ Request, Response, Status }
import com.twitter.util.Await
import org.jboss.netty.handler.codec.http.{ DefaultHttpRequest, HttpMethod, HttpVersion }
import org.scalatest._
import org.scalatest.Matchers
import app.Backend

class ApiSpec extends FlatSpec with Matchers {

  def awaitResultOf(service: Service[Request, Response]): Request => Response =
    (req) => Await.result(service(req))

  val await = awaitResultOf(Backend.api)

  def request(r: DefaultHttpRequest): Request = new Request {
    val httpRequest = r
    lazy val remoteSocketAddress = new java.net.InetSocketAddress(0)
  }

  def GET(path: String): Request =
    request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path))

  "API" should "allow GET on status route" in {
    await(GET("/zdavep")).status shouldBe Status.Ok
  }

  it should "return NotFound status for unknown route: /foo/bar" in {
    await(GET("/foo/bar")).status shouldBe Status.NotFound
  }
}
