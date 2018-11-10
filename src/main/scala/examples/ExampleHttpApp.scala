package examples

import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{HttpApp, Route}

import scala.concurrent.Future

object ExampleHttpApp extends HttpApp {

  import scala.concurrent.ExecutionContext.Implicits._

  val userService: UserService = MockUserService

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def routes(): Route =
    (get & path("user" / Segment)) { email =>
      // メールアドレスを元に、ユーザを返す
      onComplete(userService.getUser(email)) { user =>
        complete(user)
      }
    } ~ authenticateBasicAsync("akka http sample", userAuthenticator) { user =>
      // この内部は認証されたユーザしかアクセスできない
      (post & path("change-age" / IntNumber)) { age =>
        // ログイン中ユーザの年齢を変更する
        onComplete(userService.changeAge(user.email, age)) { _ =>
          complete("OK")
        }
      }
    }

  def userAuthenticator(credential: Credentials): Future[Option[User]] = {
    credential match {
      case p@Credentials.Provided(identifier) =>
        for {
          user <- userService.getUser(identifier)
        } yield {
          user.filter(u => p.verify(u.password))
        }
      case _ => Future.successful(None)
    }
  }
}

