package net.cherry.server.event

import com.redis._
import serialization._
import Parse.Implicits._
import spray.json._
import net.cherry.domain.infrastructure.json.ConversationJsonProtocol._
import net.cherry.domain.infrastructure.json.EventJsonProtocol._
import net.cherry.domain.model.conversation.{ConversationId, Conversation}
import net.cherry.infrastructure.uuid.{UUID, StatusType}
import net.cherry.domain.model.event._
import net.cherry.domain.model.event.EventTarget
import net.cherry.server.infrastructure.EventQueue
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val redis = new RedisClient("localhost", 6379)

  val x = Conversation(ConversationId(UUID()), StatusType(0), "name").toJson
  val t = """{"identity":"e6f221f5-9eec-4959-8144-ec13c9acb87a","status":0,"name":"name"}"""
  val c = JsonParser(t).convertTo[Conversation]

  val y = redis.rpop[String]("json")

  val event = Event(
    EventId(UUID()),
    StatusType(0),
    EventTarget(EventTargetType.CONVERSATION, UUID()),
    EventContentType.CONVERSATION_CREATE,
    ConversationCreateEventContent()
  )
  val eventJson = event.toJson

  println(eventJson)

  val eventQueue = EventQueue.ofRedis(redis)

  eventQueue.enqueue(event).map {
    e =>
      println('over)
  }

  /*
  eventQueue.dequeue.map {
    event =>
      println(event)
  }
  */

}
