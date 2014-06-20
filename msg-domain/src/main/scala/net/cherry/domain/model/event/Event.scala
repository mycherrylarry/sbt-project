package net.cherry.domain.model.event

import net.cherry.infrastructure.uuid.{StatusType, UUID, Entity}

trait EventId extends UUID

object EventId {
  def apply(v: String): EventId = new EventId {
    val value: String = v
  }
}

trait Event extends Entity[EventId] {
  val target: EventTarget
  val contentType: EventContentType.Value
  val content: EventContent

  def copy
  (id: EventId = this.id,
   status: StatusType.Value = this.status,
   target: EventTarget = this.target,
   contentType: EventContentType.Value = this.contentType,
   content: EventContent = this.content) =
    new EventImpl(id, status, target, contentType, content)
}

object Event {
  def apply
  (id: EventId, status: StatusType.Value, target: EventTarget, contentType: EventContentType.Value, content: EventContent): Event =
    new EventImpl(id, status, target, contentType, content)
}

