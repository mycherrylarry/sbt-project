package net.cherry.server.api.application.controller

import net.cherry.domain.model.conversation.ConversationId
import scala.concurrent.Future
import net.cherry.server.infrastructure.EventQueue

trait Conversation {
  def create: Future[Conversation]

  def join(conversationId: ConversationId): Future[Conversation]

  def sendMessage(conversationId: ConversationId, message: String): Future[Unit]
}

object Conversation {
  def apply(eventQueue: EventQueue): Conversation =
    new ConversationImpl(eventQueue)
}
