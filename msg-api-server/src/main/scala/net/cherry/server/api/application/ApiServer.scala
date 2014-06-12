package net.cherry.server.api.application

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http

object ApiServer extends App {

  /*
  implicit val system = ActorSystem("api-server")

  val route = system.actorOf(Props[Router], "api-route")

  IO(Http) ! Http.Bind(route, interface = "localhost", port = 8080)
  */

}
