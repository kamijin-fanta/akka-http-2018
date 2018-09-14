
import akka.http.scaladsl.model.{HttpMethod, HttpMethods}
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server._

object DirectivesCompose extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  val getOrPost: Directive0 = get | post
  val getUser: Directive1[String] = get & pathPrefix("user" / Segment)

  override def routes: Route =
    pathPrefix("foo") {
      getOrPost {
        complete("foo content")
      }
    } ~ getUser { userName =>
      complete(s"get user request $userName")
    } ~ {
      complete("other method request")
    }
}


object DirectivesMap extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  val getUser: Directive1[String] = get & pathPrefix("user" / Segment)
  val getUserUpperCase: Directive1[String] = getUser.map(_.toUpperCase)

  override def routes: Route =
    getUserUpperCase { userName =>
      complete(s"UpperCase: $userName")
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

  override def routes: Route =
    ctx => ctx.request.method match {
      case HttpMethods.GET => ctx.complete("get content")
      case HttpMethods.POST => ctx.complete("post content")
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

object DirectivesInternal4 extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }
val hoge: Directive1[String] = get & pathPrefix("user" / Segment)
  override def routes: Route =
    customMethodExtractor { method =>
      complete(s"bar content $method")
    }

  def customMethodExtractor: Directive1[HttpMethod] = new Directive {
    override def tapply(f: Tuple1[HttpMethod] => Route): Route =
      ctx => f(ctx.request.method)(ctx)
  }
}
