package net.cherry.domain.model.event

import net.cherry.infrastructure.uuid.{StatusType, UUID, Entity}

case class EventId() extends UUID

trait Event extends Entity[EventId] {
}

object Event {
  def apply
  (id: EventId, status: StatusType.Value) =
    new EventImpl(id, status)
}

