package net.cherry.server.api.application

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class Router extends Actor with MyService {
  override def preStart() {
    println("pre start")
  }
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}

trait MyService extends HttpService {
  val myRoute =
    path("") {
      get {
        respondWithStatus(StatusCodes.OK) {
          complete {
            "hello world"
          }
        }
      }
    } ~
    path("ticket") {
      get {
        respondWithStatus(StatusCodes.OK) {
          complete {
            "hi"
          }
        }
      }
    }
}
