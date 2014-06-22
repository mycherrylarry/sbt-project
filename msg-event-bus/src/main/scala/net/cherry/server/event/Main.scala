package net.cherry.server.event

import com.redis._
import net.cherry.infrastructure.uuid.{UUID, StatusType}
import net.cherry.domain.model.event._
import net.cherry.domain.model.event.EventTarget
import net.cherry.server.infrastructure.EventQueue
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import net.cherry.domain.model.conversation.ConversationId

object Main extends App {

  val redis = new RedisClient("localhost", 6379)

  val event = Event(
    EventId(UUID()),
    StatusType(0),
    EventTarget(EventTargetType.CONVERSATION, UUID()),
    EventContentType.MESSAGE_SEND,
    //ConversationCreateEventContent("conversation_name")
    //ConversationJoinEventContent(ConversationId(UUID()))
    MessageSendEventContent(ConversationId(UUID()), "message")
  )

  val eventQueue = EventQueue.ofRedis(redis)

  val y = eventQueue.enqueue(event).flatMap {
    x =>
      eventQueue.dequeue.map {
        result =>
          println(result)
      }
  }
  y onComplete {
    case _ => println("finish")
  }

  Await.result(y, Duration.Inf)

}
