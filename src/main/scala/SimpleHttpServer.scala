import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ HttpApp, Route }
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

object HttpServerBasic {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      get {
        path("hello") {
          complete(HttpEntity("hello"))
        } ~ path("world") {
          complete(HttpEntity("world"))
        }
      }
    val binding: Future[Http.ServerBinding] =
      Http().bindAndHandle(route, "localhost", 8080)
    println(s"Start: http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    binding
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

object HttpServerUseHttpApp {
  def main(args: Array[String]): Unit =
    WebServer.startServer("localhost", 8080)

  object WebServer extends HttpApp {
    override def routes: Route =
      path("hello") {
        get {
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            "<h1>hello akka-http</h1>"))
        }
      }
  }
}

object HttpServerHttpAppShorthand extends HttpApp {
  def main(args: Array[String]): Unit =
    startServer("localhost", 8080)

  override def routes: Route =
    path("hello") {
      get {
        complete(HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          "<h1>Say hello to akka-http</h1>"))
      }
    }
}
