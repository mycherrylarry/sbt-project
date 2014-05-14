case class EventPublisher(eventHandler: EventHandler) {
  def publish(message: String) {
    eventHandler.handleEvent(message)
  }
}