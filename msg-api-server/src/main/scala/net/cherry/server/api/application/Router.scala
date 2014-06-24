package net.cherry.server.api.application

import akka.actor.Actor
import spray.routing._
import spray.http._
import java.util.{UUID => JUUID}
import net.cherry.infrastructure.uuid.{UUID => CUUID}
import net.cherry.domain.model.conversation.ConversationId
import net.cherry.server.api.application.controller.Conversation
import com.redis.RedisClient
import net.cherry.server.infrastructure.EventQueue

class Router extends Actor with MyService {
  override def preStart() {
    println("pre start")
  }

  def actorRefFactory = context

  def receive = runRoute(myRoute)
}

trait MyService extends HttpService {
  implicit def convertUUID(javaUUID: JUUID): CUUID = CUUID(javaUUID.toString)

  lazy val redis = new RedisClient("localhost", 6379)

  lazy val eventQueue = EventQueue.ofRedis(redis)

  lazy val conversationController: Conversation = Conversation(eventQueue)

  val myRoute: Route =
    path("") {
      get {
        respondWithStatus(StatusCodes.OK) {
          complete {
            "hello world"
          }
        }
      }
    } ~
      pathPrefix("v1" / "conversation" / JavaUUID) {
        id =>
          path("send_message") {
            get {
              parameters('message.as[String]) {
                message =>
                  conversationController.sendMessage(ConversationId(id), message)
                  respondWithStatus(StatusCodes.OK) {
                    complete {
                      "OK"
                    }
                  }
              }
            }
          }
      }
}
