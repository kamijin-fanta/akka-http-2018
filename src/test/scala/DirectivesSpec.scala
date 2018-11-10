import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class DirectivesSpec extends FunSpec with ScalatestRouteTest {
  it("compose") {
    val route = DirectivesCompose.routes

    Get("/foo") ~> route ~> check {
      assert(responseAs[String] === "foo content")
    }
    Post("/foo") ~> route ~> check {
      assert(responseAs[String] === "foo content")
    }
    Get("/user/nemu") ~> route ~> check {
      assert(responseAs[String] === "get user: nemu")
    }
    Get("/other") ~> route ~> check {
      assert(responseAs[String] === "other method request")
    }
  }

  it("map") {
    val route = DirectivesMap.routes
    Get("/user/hoge") ~> route ~> check {
      assert(responseAs[String] === "UpperCase: HOGE")
    }
  }

  it("tmap") {
    val route = DirectivesTMap.routes
    Get("/domain-name/user-name") ~> route ~> check {
      assert(responseAs[String] === "mail: user-name@domain-name")
    }
  }

  it("require") {
    val route = DirectivesRequire.routes
    Get("https://example.com/") ~> route ~> check {
      assert(responseAs[String] === "example.com content")
    }
  }
}
