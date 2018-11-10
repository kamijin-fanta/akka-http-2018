
import akka.http.scaladsl.model.StatusCodes.Redirection
import akka.http.scaladsl.model.{ ContentRange, HttpMethod, HttpMethods, Uri }
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.headers.{ Authorization, Host, HttpOrigin, HttpOriginRange }
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.Credentials

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
      complete(s"get user: $userName")
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

object DirectivesTMap extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  val domainAndUser: Directive[(String, String)] =
    pathPrefix(Segment / Segment)
  val mailAddress: Directive1[String] = domainAndUser.tmap {
    case (domain, user) => s"$user@$domain"
  }

  override def routes: Route =
    mailAddress { mailAddress =>
      complete(s"mail: $mailAddress")
    }
}

object DirectivesRequire extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  def customHost(hostname: String): Directive0 =
    extractHost.require(_ == hostname)

  override def routes: Route =
    customHost("example.com") {
      complete(s"example.com content")
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

  override def routes: Route =
    customMethodExtractor { method =>
      complete(s"bar content $method")
    }

  def customMethodExtractor: Directive1[HttpMethod] = new Directive[Tuple1[HttpMethod]] {
    override def tapply(f: Tuple1[HttpMethod] => Route): Route =
      ctx => f(Tuple1(ctx.request.method))(ctx)
  }
}
