
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server._

object DirectivesCompose extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def routes: Route =
    pathPrefix("foo") {
      get {
        complete("get foo content")
      } ~ post {
        complete("post foo content")
      }
    } ~ (get & extractUri) { uri =>
      complete(s"hoge request ${uri.path}")
    } ~ {
      complete("other method request")
    }
}

object DirectivesInternal1 extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def routes: Route =
    (ctx: RequestContext) => ctx.complete("ok")
}

object DirectivesInternal2 extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  // type Route = RequestContext ⇒ Future[RouteResult]
  override def routes: Route =
    ctx => ctx.request.uri.path.toString match {
      case "/foo" => ctx.complete("/foo")
      case "/error" => ctx.reject()
      case _ => ctx.reject()
    }
}

object DirectivesInternal3 extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def routes: Route =
    pathPrefix("foo") {
      complete("foo content")
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
