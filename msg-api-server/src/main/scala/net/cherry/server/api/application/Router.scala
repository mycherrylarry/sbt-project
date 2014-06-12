package net.cherry.server.api.application

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class Router extends Actor with MyService {
  def actorRefFactory = context

  def receive = runRoute(myRoute)
}

trait MyService extends HttpService {
  val myRoute =
    path("/a") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            "hello world"
          }
        }
      }
    }
}
