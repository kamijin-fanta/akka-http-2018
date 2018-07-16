import akka.http.scaladsl.server.{HttpApp, Route}

object HowToDirectives2 {
  def main(args: Array[String]): Unit = {
    WebServer.startServer("localhost", 8080)
  }

  object WebServer extends HttpApp {
    override def routes: Route =
      ctx => ctx.request.uri.path.toString match {
        case "/foo" => ctx.complete("/foo")
        case "/bar" => ctx.complete("/bar")
        case "/error" => ctx.reject(throw new Exception("ERROR"))
        case path => ctx.complete(s"not found $path")
      }
  }
}
