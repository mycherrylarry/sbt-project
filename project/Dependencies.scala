import sbt._
trait Dependencies {
  val finagleVersion = "6.2.0"

  val finagleHttp = "com.twitter" %% "finagle-http" % finagleVersion
  val finagleStream = "com.twitter" %% "finagle-stream" % finagleVersion
}