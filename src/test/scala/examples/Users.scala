package examples

import org.scalatest.AsyncFunSpec

class MockUserServiceSpec extends AsyncFunSpec {
  it("auth success") {
    MockUserService
      .auth("hoge@example.com", "hogehoge")
      .map { user =>
        assert(user.isDefined)
        assert(user.get.email === "hoge@example.com")
      }
  }
  it("auth failed") {
    MockUserService
      .auth("hoge@example.com", "invalid password")
      .map { user =>
        assert(user.isEmpty)
      }
  }
  it("getUser found") {
    MockUserService
      .getUser("hoge@example.com")
      .map { user =>
        assert(user.isDefined)
        assert(user.get.email === "hoge@example.com")
      }
  }
  it("getUser not found") {
    MockUserService
      .getUser("invalid-email@example.com")
      .map { user =>
        assert(user.isEmpty)
      }
  }
  it("changeAge") {
    for {
      newHoge <- MockUserService
        .changeAge("hoge@example.com", 50)
      getUserResult <- MockUserService.getUser("hoge@example.com")
    } yield {
      assert(newHoge.isDefined)
      assert(newHoge.get.age === 50)
      assert(getUserResult.isDefined)
      assert(getUserResult.get.age === 50)
    }
  }
}
