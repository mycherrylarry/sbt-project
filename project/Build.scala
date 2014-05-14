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
  ) dependsOn(infrastructure,test % "test") aggregate(infrastructure)

}