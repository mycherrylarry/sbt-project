import sbt._

trait Dependencies {
  val finagleVersion = "6.2.0"

  val finagleHttp = "com.twitter" %% "finagle-http" % finagleVersion
  val finagleStream = "com.twitter" %% "finagle-stream" % finagleVersion

  val akkaVersion = "2.1.4"
  val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion

  val sprayVersion = "1.0.1"
  val sprayCan = "io.spray" % "spray-can" % sprayVersion
  val sprayRouting = "io.spray" % "spray-routing" % sprayVersion
  val sprayTest = "io.spray" % "spray-testkit" % sprayVersion

  val commonResolvers = Seq(
    "twitter" at "http://maven.twttr.com",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "spray repo" at "http://repo.spray.io",
    "spray nightlies repo" at "http://nightlies.spray.io"
  )
}
