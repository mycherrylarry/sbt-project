package net.cherry.domain.model.event

import net.cherry.infrastructure.uuid.UUID


trait Event {
  val id: UUID
}

object Event {
  def apply(id: UUID) =
    new EventImpl(id)
}

