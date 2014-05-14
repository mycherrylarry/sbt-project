package net.cherry.server.infrastructure

/**
 * publish event
 *
 * @param eventHandler
 */
case class EventPublisher(eventHandler: EventHandler) {
  def publish(message: String) = {
    eventHandler.handleEvent(message)
  }
}