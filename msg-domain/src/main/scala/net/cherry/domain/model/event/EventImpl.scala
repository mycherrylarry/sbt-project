package net.cherry.domain.model.event

import net.cherry.infrastructure.uuid.StatusType

class EventImpl
(val id: EventId,
 val status: StatusType.Value,
 val target: EventTarget,
 val contentType: EventContentType.Value,
 val content: EventContent)
  extends Event
