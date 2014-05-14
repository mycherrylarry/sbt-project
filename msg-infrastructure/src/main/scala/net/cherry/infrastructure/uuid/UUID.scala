package net.cherry.infrastructure.uuid

trait UUID

object UUID extends UUID {
  def value = java.util.UUID.randomUUID().toString
}
