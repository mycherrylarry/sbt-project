package net.cherry.domain.model.user

import net.cherry.infrastructure.uuid.StatusType

class UserImpl
(val id: UserId,
 val status: StatusType.Value,
 val name: String)
  extends User
