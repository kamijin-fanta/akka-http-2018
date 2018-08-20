name := "akka-http-2018"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(  // 1. akka-http関連
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test,
  "com.typesafe.akka" %% "akka-actor" % "2.5.14",
  "com.typesafe.akka" %% "akka-stream" % "2.5.14",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.14" % Test,
)

libraryDependencies ++= Seq(  // 2. テストライブラリ
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

libraryDependencies ++= Seq(  // 3. JSON関連
  "io.circe" %% "circe-core" % "0.9.3",
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-parser" % "0.9.3"
)
