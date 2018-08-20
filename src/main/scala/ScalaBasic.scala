object ScalaBasic {
  def main(args: Array[String]): Unit = {
    println("1. ------ 変数")
    val immutableVariable: String = "string literal" // 不変な変数を宣言　型を明示
    var mutableIntVariable = 100 // 変更可能な変数を宣言　型はコンパイル時に推論させる
    // immutableVariable = "hoge" // valで宣言したので、変更しようとするとエラーになる
    mutableIntVariable = 150
    println(immutableVariable)
    println(mutableIntVariable)

    println("2. ------ 関数・メソッド")
    val doubleFn: Int => Int = (n: Int) => {
      n * 2 // 最後の値が自動的に戻り値として使用される
    }
    def doubleMethod(n: Int) = {
      n * 2
    }
    val fnShortHand = (n: Int) => n * 2 // 単一の式であれば、{}は不要
    val fnResult = doubleFn(10)
    println(s"place holder $fnResult / ${doubleMethod(10)}") // s""を使用することでプレスホルダになる

    println("3. ------ タプル")
    val tuple = (100, "value", (200, "nest tuple")) // 数個の変数をまとめる事ができる　中身は不変
    val tupleArrowed = 100 -> "value" // 要素が2つのタプルは"->"で宣言可能
    println(s"tuple: $tuple / $tupleArrowed")

    println("4. ------ if条件")
    val fooValue = 200
    val ifResult =
      if (fooValue == 100) "is 100"
      else "not 100" // ifも式なので、値を返す
    println(ifResult)

    println("5. ------ パターンマッチング")
    val foo = 110
    val matchingResult = foo match { // マッチングを記述できる　条件は上から順に見る
      case 10 => "value is 10" // 値のみ記述すると、同一の場合のみマッチング
      case x if x < 100 => "less than 100" // 変数に格納後、条件を指定
      case x: Int if x > 100 => "more than 100" // 条件に型を含めることも可能
      case x => s"other $x" // すべてのケースに一致しなかった場合にマッチ
    }
    println(matchingResult)

    println("6. ------ Option")
    val some: Option[String] = Some("string")
    val none: Option[String] = None
    println(s"some: ${some.getOrElse("---")} / none: ${none.getOrElse("---")}")
  }
}
