import sbt._
import Keys._

object ServerBuild extends Build with Dependencies {

  lazy val commonSettings = Project.defaultSettings

  lazy val test = Project(
    id = "msg-test",
    base = file("msg-test"),
    settings = commonSettings ++ Seq(
      name := "msg-test",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  )

  lazy val infrastructure = Project(
    id = "msg-infrastructure",
    base = file("msg-infrastructure"),
    settings = commonSettings ++ Seq(
      name := "msg-infrastructure",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn (test % "test")

  lazy val domain = Project(
    id = "msg-domain",
    base = file("msg-domain"),
    settings = commonSettings ++ Seq(
      name := "msg-domain",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn (test % "test")

  lazy val streamServer = Project(
    id = "msg-stream-server",
    base = file("msg-stream-server"),
    settings = commonSettings ++ Seq(
      name := "msg-stream-server",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
        finagleHttp,
        finagleStream
      )
    )
  ) dependsOn(infrastructure, domain, test % "test") aggregate(infrastructure, domain)

  lazy val apiServer = Project(
    id = "msg-api-server",
    base = file("msg-api-server"),
    settings = commonSettings ++ Seq(
      name := "msg-api-server",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn(infrastructure, domain, test % "test") aggregate(infrastructure, domain)

  lazy val eventBus = Project(
    id = "msg-event-bus",
    base = file("msg-event-bus"),
    settings = commonSettings ++ Seq(
      name := "msg-event-bus",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn(infrastructure, domain, test % "test") aggregate(infrastructure, domain)

}