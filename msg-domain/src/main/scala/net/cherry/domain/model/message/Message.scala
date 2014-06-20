package net.cherry.domain.model.message

import net.cherry.infrastructure.uuid.{StatusType, UUID, Entity}

trait MessageId extends UUID

object MessageId {
  def apply(v: String): MessageId = new MessageId {
    val value: String = v
  }
}

trait Message extends Entity[MessageId] {
  val content: Array[Byte]

  def copy
  (id: MessageId = this.id,
   status: StatusType.Value = this.status,
   content: Array[Byte] = this.content) = new MessageImpl(id, status, content)
}

object Message {
  def apply
  (id: MessageId, status: StatusType.Value, content: Array[Byte]): Message =
    new MessageImpl(id, status, content)
}
