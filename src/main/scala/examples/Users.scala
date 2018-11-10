package examples

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import io.circe.generic.auto._
import io.circe.syntax._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

case class User(email: String, name: String, age: Int, password: String)

object User {
  implicit val userMarshaller: ToEntityMarshaller[User] = Marshaller
    .opaque { user =>
      HttpEntity(ContentTypes.`application/json`, user.asJson.noSpaces)
    }
}

trait UserService {
  def getUser(email: String): Future[Option[User]]

  def changeAge(email: String, newAge: Int): Future[Option[User]]
}

object MockUserService extends UserService {

  // mutable
  val userList = ListBuffer(
    User("hoge@example.com", "hoge", 24, "hogehoge"),
    User("piyo@example.com", "piyo", 28, "piyopiyo"),
    User("bar@example.com", "bar", 21, "barbar"),
  )

  override def getUser(email: String): Future[Option[User]] = {
    val user = userList.find(u => u.email == email)
    Future.successful(user)
  }

  override def changeAge(email: String, newAge: Int): Future[Option[User]] = {
    val user = userList.find(u => u.email == email)
    val newUser = user.map(_.copy(age = newAge))

    for {
      u <- user
      n <- newUser
    } yield {
      val position = userList.indexOf(u)
      userList.update(position, n)
    }

    Future.successful(newUser)
  }
}
