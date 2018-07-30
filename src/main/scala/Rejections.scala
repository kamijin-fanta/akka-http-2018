import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, StatusCodes }
import akka.http.scaladsl.server._

object RejectionsBasic extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  implicit def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case reject: MissingQueryParamRejection =>
          complete(StatusCodes.BadRequest, s"required query parameter [${reject.parameterName}]")
      }
      .result()

  override def routes: Route = Route.seal(internalRoutes)

  def internalRoutes: Route =
    pathPrefix("hoge") {
      complete("hoge")
    } ~ pathPrefix("foo") {
      get {
        complete("get foo")
      } ~ post {
        complete("post foo")
      }
    } ~ (pathPrefix("query-example") & parameter('name)) { q =>
      complete(s"name: $q")
    }
}

object RejectionsCustom extends HttpApp {
  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  case class AssertRejection(filed: String, message: String) extends Rejection

  implicit def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case reject: AssertRejection =>
          complete(
            StatusCodes.BadRequest,
            HttpEntity(
              ContentTypes.`application/json`,
              s"""{"error": {"filed": "${reject.filed}", "message": "${reject.message}"}"""))
      }
      .result()

  override def routes: Route = Route.seal(internalRoutes)

  def internalRoutes: Route =
    (post & path("user" / Segment)) {
      case userName if userName.length < 3 =>
        reject(AssertRejection("user", "should be more then 3"))
      case userName if userName.length > 10 =>
        reject(AssertRejection("user", "should be less then 10"))
      case userName =>
        complete(s"Success: $userName")
    }
}
