import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{HttpApp, Route}

object HowToDirectivesCompose {
  def main(args: Array[String]): Unit = {
    WebServer.startServer("localhost", 8080)
  }

  object WebServer extends HttpApp {
    override def routes: Route =
      pathPrefix("foo") {
        get {
          complete("get foo content")
        } ~ post {
          complete("post foo content")
        } ~ {
          complete(StatusCodes.MethodNotAllowed, "method not allowed")
        }
      } ~ (get & extractUri) { uri =>
        complete(s"hoge request ${uri.path}")
      } ~ {
        complete("other method request")
      }
  }
}
