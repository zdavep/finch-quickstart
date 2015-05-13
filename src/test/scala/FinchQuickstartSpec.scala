import com.twitter.finagle.Service
import com.twitter.finagle.httpx.Request
import com.twitter.util.Await
import io.finch._
import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, HttpMethod, HttpVersion}
import org.scalatest._
import org.scalatest.Matchers
import zdavep.Quickstart

class FinchQuickstartSpec extends FlatSpec with Matchers {

  def awaitResultOf(service: Service[HttpRequest, HttpResponse]): Request => HttpResponse =
    (req) => Await.result(service(req))

  val await = awaitResultOf(Quickstart.backend)

  def request(r: DefaultHttpRequest): Request = new Request {
      val httpRequest = r
      lazy val remoteSocketAddress = new java.net.InetSocketAddress(0)
  }

  def GET(path: String): Request =
    request(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path))

  "Quickstart Service" should "allow GET on greeting endpoint" in {
    await(GET("/quickstart/api/v1/greeting/Finch")).status shouldBe io.finch.response.Ok().status
  }

  it should "return NotFound status for unknown route: /foo/bar" in {
    await(GET("/foo/bar")).status shouldBe io.finch.response.NotFound.status
  }
}
