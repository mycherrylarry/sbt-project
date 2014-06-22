package net.cherry.domain.model.event

import net.cherry.domain.model.conversation.ConversationId

trait EventContent

case class ConversationCreateEventContent(conversationName: String) extends EventContent

case class ConversationJoinEventContent(conversationId: ConversationId) extends EventContent

case class MessageSendEventContent(conversationId: ConversationId, message: String) extends EventContent

object EventContentType extends Enumeration {
  val CONVERSATION_CREATE,
  CONVERSATION_JOIN,
  MESSAGE_SEND = Value
}
