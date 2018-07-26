import akka.http.scaladsl.model.Uri.{ Host, Query }
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{ HttpOrigin, RawHeader }
import org.scalatest.FunSpec

class DataModelSpec extends FunSpec {
  it("uri") {
    assert(Uri("https://akka.io:443/try-akka/") ===
      Uri.from(scheme = "https", host = "akka.io", port = 443, path = "/try-akka/"))

    assert(Query("key1=value1&key2=value2").get("key1") === Some("value1"))
  }
  it("httpRequest") {
    val tryAkkaUri = Uri("https://akka.io:443/try-akka/")
    HttpRequest(
      method = HttpMethods.GET,
      uri = tryAkkaUri)

    HttpRequest(
      method = HttpMethods.PUT,
      uri = Uri("http://localhost/user"),
      entity = HttpEntity(
        ContentTypes.`application/json`,
        """{"id": 1, "name": "nemu"}"""))
  }
  it("header") {
    RawHeader("x-custom-header", "value")
    headers.Host("example.com")
    headers.`Access-Control-Allow-Origin`(HttpOrigin("https://example.com"))
  }
}
