import sbt._
import Keys._

object ServerBuild extends Build with Dependencies {

  lazy val commonSettings = Project.defaultSettings

  lazy val test = Project(
    id = "test",
    base = file("test"),
    settings = commonSettings ++ Seq(
      name := "test",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  )

  lazy val infrastructure = Project(
    id = "infrastructure",
    base = file("infrastructure"),
    settings = commonSettings ++ Seq(
      name := "infrastructure",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn (test % "test")

  lazy val domain = Project(
    id = "domain",
    base = file("domain"),
    settings = commonSettings ++ Seq(
      name := "domain",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn (test % "test")

  lazy val streamServer = Project(
    id = "stream-server",
    base = file("stream-server"),
    settings = commonSettings ++ Seq(
      name := "stream-server",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
        finagleHttp,
        finagleStream
      )
    )
  ) dependsOn(infrastructure, domain, test % "test") aggregate(infrastructure, domain)

  lazy val apiServer = Project(
    id = "api-server",
    base = file("api-server"),
    settings = commonSettings ++ Seq(
      name := "api-server",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn(infrastructure, domain, test % "test") aggregate(infrastructure, domain)

  lazy val eventBus = Project(
    id = "event-bus",
    base = file("event-bus"),
    settings = commonSettings ++ Seq(
      name := "event-bus",
      organization := "net.cherry",
      libraryDependencies ++= Seq(
      )
    )
  ) dependsOn(infrastructure, domain, test % "test") aggregate(infrastructure, domain)

}