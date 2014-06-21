package net.cherry.infrastructure.uuid

case class UUID(id: String = java.util.UUID.randomUUID().toString) {
  override def toString(): String = id
}
