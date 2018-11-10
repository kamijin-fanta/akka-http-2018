package examples

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSpec

class ExampleHttpAppSpec extends FunSpec with ScalatestRouteTest {
  it("get user") {
    Get("/user/hoge@example.com") ~> ExampleHttpApp.routes() ~> check {
      assert(status === StatusCodes.OK)
      assert(responseAs[String] === """{"email":"hoge@example.com","name":"hoge","age":24,"password":"hogehoge"}""")
    }
  }
  it("change-age") {
    Post("/change-age/hoge@example.com/50") ~> ExampleHttpApp.routes() ~> check {
      assert(status === StatusCodes.OK)
      assert(responseAs[String] === """OK""")
    }
    Get("/user/hoge@example.com") ~> ExampleHttpApp.routes() ~> check {
      assert(status === StatusCodes.OK)
      assert(responseAs[String] === """{"email":"hoge@example.com","name":"hoge","age":50,"password":"hogehoge"}""")
    }
  }
}
