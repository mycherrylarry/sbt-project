package net.cherry.infrastructure.uuid

trait UUID {
  val value: String
}

object UUID extends UUID {
  val value = java.util.UUID.randomUUID().toString
}
