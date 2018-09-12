import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server.{ Directive, Directive0, HttpApp, Route }

object RoutingBasic extends HttpApp {

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def routes: Route =
    pathPrefix("hello") {
      get {
        complete("get hello")
      } ~ post {
        complete("post hello")
      }
    } ~ customPathPrefix("bar") {
      complete("bar content")
    }

  def customPathPrefix(string: String): Directive0 = new Directive[Unit] {
    override def tapply(inner: Unit => Route): Route = { ctx =>
      val currentPath = ctx.request.uri.path
      if (currentPath.startsWith(Path("/" + string))) inner()(ctx)
      else ctx.reject()
    }
  }
}