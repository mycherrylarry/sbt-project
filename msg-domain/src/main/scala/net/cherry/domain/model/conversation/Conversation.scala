package net.cherry.domain.model.conversation

import net.cherry.infrastructure.uuid.{StatusType, UUID, Entity}

case class ConversationId(value: UUID)

trait Conversation extends Entity[ConversationId] {
  val name: String

  def copy
  (id: ConversationId = this.id,
   status: StatusType.Value = this.status,
   name: String = this.name) = new ConversationImpl(id, status, name)
}

object Conversation {
  def apply
  (id: ConversationId, status: StatusType.Value, name: String): Conversation =
    new ConversationImpl(id, status, name)
}
