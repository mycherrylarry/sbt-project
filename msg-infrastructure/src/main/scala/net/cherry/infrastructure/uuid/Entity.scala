package net.cherry.infrastructure.uuid

trait Entity[ID <: UUID] {
  val id: ID
  val status: StatusType.Value
  def asString = id
}
