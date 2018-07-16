import akka.http.scaladsl.server.{HttpApp, RequestContext, Route}

object HowToDirectives {
  def main(args: Array[String]): Unit = {
    WebServer.startServer("localhost", 8080)
  }
  object WebServer extends HttpApp {
    override def routes: Route =
      (ctx: RequestContext) => ctx.complete("ok")
  }
}
