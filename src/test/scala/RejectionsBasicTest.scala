import akka.http.scaladsl.model.{ HttpMethods, StatusCodes }
import akka.http.scaladsl.server.{ MethodRejection, MissingQueryParamRejection }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class RejectionsBasicTest extends FunSpec with ScalatestRouteTest {
  it("basic") {
    Put("/foo") ~> RejectionsBasic.internalRoutes ~> check {
      assert(rejections === Seq(
        MethodRejection(HttpMethods.GET),
        MethodRejection(HttpMethods.POST)))
    }
    Put("/foo") ~> RejectionsBasic.routes ~> check {
      assert(status === StatusCodes.MethodNotAllowed)
      assert(responseAs[String] === "HTTP method not allowed, supported methods: GET, POST")
    }
  }
  it("not found") {
    Get("/not_found") ~> RejectionsBasic.internalRoutes ~> check {
      assert(rejections === Seq())
    }
    Get("/not_found") ~> RejectionsBasic.routes ~> check {
      assert(status === StatusCodes.NotFound)
      assert(responseAs[String] === "The requested resource could not be found.")
    }
  }

  it("custom") {
    Get("/query-example?name=mike") ~> RejectionsBasic.routes ~> check {
      assert(responseAs[String] === "name: mike")
    }
    Get("/query-example") ~> RejectionsBasic.internalRoutes ~> check {
      assert(rejections === Seq(MissingQueryParamRejection("name")))
    }
    Get("/query-example") ~> RejectionsBasic.routes ~> check {
      assert(status === StatusCodes.BadRequest)
      assert(responseAs[String] === "required query parameter [name]")
    }
  }
}

class RejectionsCustomTest extends FunSpec with ScalatestRouteTest {
  it("custom rejection types") {
    Post("/user/a") ~> RejectionsCustom.routes ~> check {
      assert(status === StatusCodes.BadRequest)
      assert(responseAs[String] === """{"error": {"filed": "user", "message": "should be more then 3"}""")
    }
    Post("/user/newton") ~> RejectionsCustom.routes ~> check {
      assert(status === StatusCodes.OK)
      assert(responseAs[String] === "Success: newton")
    }
  }
}
