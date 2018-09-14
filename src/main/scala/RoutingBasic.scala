import akka.http.scaladsl.server.{ HttpApp, Route }

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
    } ~ path("user" / Segment) { userName =>
      complete(s"UserName: $userName")
    }
}
