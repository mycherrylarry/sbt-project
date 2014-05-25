import sbt._

trait Dependencies {
  val finagleVersion = "6.2.0"

  val finagleHttp = "com.twitter" %% "finagle-http" % finagleVersion
  val finagleStream = "com.twitter" %% "finagle-stream" % finagleVersion

  val akkaVersion = "2.3.3"
  val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion

  val commonResolvers = Seq(
    "twitter" at "http://maven.twttr.com",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  )
}
