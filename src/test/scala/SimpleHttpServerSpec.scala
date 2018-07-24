import akka.http.scaladsl.model.{ ContentTypes, StatusCodes }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class SimpleHttpServerSpec extends FunSpec with ScalatestRouteTest {
  it("HttpServerHttpAppShorthand") {
    Get("/hello") ~> HttpServerHttpAppShorthand.routes ~> check {
      assert(status === StatusCodes.OK)
      assert(responseAs[String] === "<h1>Say hello to akka-http</h1>")
      assert(contentType === ContentTypes.`text/html(UTF-8)`)
    }
  }
}
