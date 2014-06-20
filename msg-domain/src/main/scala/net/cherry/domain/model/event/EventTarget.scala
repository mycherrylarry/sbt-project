package net.cherry.domain.model.event

import net.cherry.infrastructure.uuid.UUID

object EventTargetType extends Enumeration {
  val USER, CONVERSATION, SYSTEM = Value
}

case class EventTarget(target: EventTargetType.Value, id: UUID)


