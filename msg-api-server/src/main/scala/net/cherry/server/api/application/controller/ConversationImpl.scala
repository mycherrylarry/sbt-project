package net.cherry.server.api.application.controller

import scala.concurrent._
import net.cherry.server.infrastructure.EventQueue
import net.cherry.domain.model.event._
import net.cherry.infrastructure.uuid.StatusType
import net.cherry.infrastructure.uuid.UUID
import net.cherry.domain.model.conversation.ConversationId
import net.cherry.domain.model.event.EventTarget
import net.cherry.domain.model.event.EventId
import ExecutionContext.Implicits.global

class ConversationImpl
(eventQueue: EventQueue)
  extends Conversation {
  def create: Future[Conversation] = ???

  def join(conversationId: ConversationId): Future[Conversation] = ???

  def sendMessage(conversationId: ConversationId, message: String): Future[Unit] = future {
    val event = Event(
      EventId(UUID()),
      StatusType(0),
      EventTarget(EventTargetType.CONVERSATION, UUID()),
      EventContentType.MESSAGE_SEND,
      MessageSendEventContent(conversationId, message)
    )
    eventQueue.enqueue(event)
  }
}
