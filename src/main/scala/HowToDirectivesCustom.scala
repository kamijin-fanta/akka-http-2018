import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server._

object HowToDirectivesCustom {
  def main(args: Array[String]): Unit = {
    WebServer.startServer("localhost", 8080)
  }

  object WebServer extends HttpApp {
    override def routes: Route =
      pathPrefix("foo") {
        complete("foo content")
      } ~ customPathPrefix("bar") {
        complete("bar content")
      }
  }

  def customPathPrefix(string: String): Directive0 = new Directive[Unit] {
    override def tapply(inner: Unit => Route): Route = { ctx =>
      val currentPath = ctx.request.uri.path
      if (currentPath.startsWith(Path("/" + string))) inner()(ctx)
      else ctx.reject()
    }
  }
}
