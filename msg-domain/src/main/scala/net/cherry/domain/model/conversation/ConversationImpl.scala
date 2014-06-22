package net.cherry.domain.model.conversation

import net.cherry.infrastructure.uuid.StatusType

class ConversationImpl
(val id: ConversationId,
 val status: StatusType.Value,
 val name: String)
  extends Conversation
