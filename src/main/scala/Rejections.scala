import akka.http.scaladsl.server._

object RejectionsBasic extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def routes: Route =
    pathPrefix("hoge") {
      complete("hoge")
    } ~ pathPrefix("foo") {
      get {
        complete("get foo")
      } ~ post {
        complete("post foo")
      }
    }
}
