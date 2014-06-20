package net.cherry.domain.model.user

import net.cherry.infrastructure.uuid.{StatusType, UUID, Entity}

case class UserId() extends UUID

trait User extends Entity[UserId] {

  val name: String

  def copy
  (id: UserId = this.id,
   status: StatusType.Value = this.status,
   name: String = this.name) = new UserImpl(id, status, name)
}

object User {
  def apply
  (id: UserId, status: StatusType.Value, name: String) =
    new UserImpl(id, status, name)
}