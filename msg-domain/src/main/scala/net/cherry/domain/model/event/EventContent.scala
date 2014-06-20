package net.cherry.domain.model.event

import net.cherry.infrastructure.uuid.UUID

trait EventContent

case class ConversationCreateEventContent() extends EventContent

case class ConversationJoinEventContent(id: UUID) extends EventContent

case class MessageSend(message: String) extends EventContent

object EventContentType extends Enumeration {
  val CONVERSATION_CREATE,
  CONVERSATION_JOIN,
  MESSAGE_SEND = Value
}
