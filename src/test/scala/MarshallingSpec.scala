import akka.http.scaladsl.marshalling.{ Marshaller, ToEntityMarshaller }
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.FunSpec

case class User(name: String, age: Int)

class MarshallingSpec extends FunSpec with ScalatestRouteTest {
  it("basic") {
    // 1. UserオブジェクトをJson文字列に変換し、HttpEntityを生成するマーシャラーを作成
    implicit val userMarshaller: ToEntityMarshaller[User] = Marshaller
      .opaque { user =>
        HttpEntity(ContentTypes.`application/json`, user.asJson.noSpaces)
      }

    // 2. マーシャラーを作成すると、completeに対してUserオブジェクトを直接渡すことが出来る
    val route: Route = get {
      complete(User("mika", 20))
    }

    // 3. Userオブジェクトが適切にマーシャリングされ、JSON文字列としてクライアントに送信される
    Get("/") ~> route ~> check {
      assert(contentType === ContentTypes.`application/json`)
      assert(responseAs[String] === """{"name":"mika","age":20}""")
    }
  }

  it("Content-negotiation") {
    // 1. リクエストのAcceptヘッダにより、必要とされるマーシャラーを選択し、使用する
    implicit val userMarshaller: ToEntityMarshaller[User] = Marshaller.oneOf(
      Marshaller.withFixedContentType(ContentTypes.`application/json`) { user =>
        HttpEntity(ContentTypes.`application/json`, user.asJson.noSpaces)
      },
      Marshaller.withFixedContentType(ContentTypes.`text/plain(UTF-8)`) { user =>
        val text = s"name: ${user.name}, age: ${user.age}"
        HttpEntity(ContentTypes.`text/plain(UTF-8)`, text)
      })

    val route: Route = get {
      complete(User("mika", 20))
    }

    // 2. JSONをAcceptヘッダに含めてリクエストするとJSON文字列が得られる
    val jsonRequest = HttpRequest(headers = List(Accept(MediaTypes.`application/json`)))
    jsonRequest ~> route ~> check {
      assert(contentType === ContentTypes.`application/json`)
      assert(responseAs[String] === """{"name":"mika","age":20}""")
    }

    // 3. textをAcceptヘッダに含めてリクエストするとテキストが得られる
    val textRequest = HttpRequest(headers = List(Accept(MediaTypes.`text/plain`)))
    textRequest ~> route ~> check {
      assert(contentType === ContentTypes.`text/plain(UTF-8)`)
      assert(responseAs[String] === """name: mika, age: 20""")
    }
  }
}
