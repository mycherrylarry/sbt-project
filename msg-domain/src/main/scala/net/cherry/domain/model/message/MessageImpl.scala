package net.cherry.domain.model.message

import net.cherry.infrastructure.uuid.StatusType

class MessageImpl
(val id: MessageId,
 val status: StatusType.Value,
 val content: Array[Byte])
  extends Message
