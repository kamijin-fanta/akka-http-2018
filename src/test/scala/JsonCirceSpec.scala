import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import org.scalatest.FunSpec

class JsonCirceSpec extends FunSpec {
  it("basic") {
    ////// 1. パース
    val rawJson: String = """
      {
        "foo": "bar",
        "baz": 123,
        "list": [ 4, 5, 6 ]
      }
    """
    // JSON文字列をパースし、パースに失敗すればJsonオブジェクトとしてのNullとする
    val json: Json = parse(rawJson).getOrElse(Json.Null)
    // カーソルを使用して、特定のフィールドを抽出
    assert(json.hcursor.downField("foo").as[String] === Right("bar"))

    ////// 2. 型への抽出
    assert(json.as[ExampleData] === Right(ExampleData("bar", 123, List(4, 5, 6))))

    ////// 3. カーソルを使用してJSONの編集
    val cursor: HCursor = json.hcursor
    val result: Option[Json] = cursor
      .downField("foo")
      .withFocus(_.mapString(_.reverse))
      .top
    // jsonオブジェクトのを文字列に変換する
    assert(result.map(_.noSpaces) === Some("""{"foo":"rab","baz":123,"list":[4,5,6]}"""))
  }
}

case class ExampleData(foo: String, baz: Int, list: List[Int])
