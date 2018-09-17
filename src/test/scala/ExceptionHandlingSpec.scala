import akka.http.scaladsl.model.{ HttpResponse, StatusCodes }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ ExceptionHandler, Route }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class ExceptionHandlingSpec extends FunSpec with ScalatestRouteTest {
  it("handle exception") {
    implicit val exceptionHandler = ExceptionHandler {
      case _: ArithmeticException =>
        extractUri { uri =>
          complete(HttpResponse(
            StatusCodes.BadRequest,
            entity = s"error on ${uri.path}"))
        }
    }
    val route = path("div" / IntNumber / IntNumber) {
      (a, b) => complete(s"ans: ${a / b}")
    }
    val sealedRoute = Route.seal(route)

    Get("/div/10/2") ~> sealedRoute ~> check {
      assert(responseAs[String] === "ans: 5")
    }
    Get("/div/10/0") ~> sealedRoute ~> check {
      assert(status === StatusCodes.BadRequest)
      assert(responseAs[String] === "error on /div/10/0")
    }
  }
}
