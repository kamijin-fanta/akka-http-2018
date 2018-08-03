import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.scalatest.{ BeforeAndAfterAll, FunSpec }

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContextExecutor, Future }

class ClientSideSpec extends FunSpec with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executeContext: ExecutionContextExecutor = system.dispatcher

  var bind: ServerBinding = _

  override protected def beforeAll(): Unit = {
    bind = Await.result(Http().bindAndHandle(routes, "localhost", 8080), 10 seconds)
  }

  it("get content") {
    val req = HttpRequest(
      method = HttpMethods.GET,
      uri = "http://localhost:8080/user")
    val futureResponse: Future[HttpResponse] =
      Http().singleRequest(req)

    val res: HttpResponse = Await.result(futureResponse, 2 seconds)
    assert(res.status === StatusCodes.OK)
  }

  override protected def afterAll(): Unit = {
    Await.result(bind.unbind(), 10 seconds)
    Await.result(system.terminate(), 10 seconds)
  }

  def routes: Route =
    (get & path("user")) {
      complete("User")
    }
}
