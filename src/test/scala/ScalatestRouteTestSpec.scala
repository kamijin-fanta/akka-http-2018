import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.directives.MethodDirectives._
import akka.http.scaladsl.server.directives.RouteDirectives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class ScalatestRouteTestSpec extends FunSpec with ScalatestRouteTest {
  it("basic") {
    assert(6 === 3 * 2)
    assertThrows[ArithmeticException] {
      10 / 0
    }
  }

  it("route test") {
    val route = get {
      complete("ok")
    }

    Get("/") ~> route ~> check {
      assert(contentType === ContentTypes.`text/plain(UTF-8)`)
      assert(responseAs[String] === "ok")
    }
  }
}
