import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class RejectionsBasicTest extends FunSpec with ScalatestRouteTest {
  it("basic") {
    Put("/foo") ~> Route.seal(RejectionsBasic.routes) ~> check {
      assert(rejections === Seq())
    }
  }
}
