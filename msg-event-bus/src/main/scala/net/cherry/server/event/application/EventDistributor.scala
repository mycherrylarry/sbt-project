package net.cherry.server.event.application

import akka.actor.Actor
import akka.event.Logging
import net.cherry.domain.model.event.Event
import net.cherry.server.infrastructure.EventQueue

// TODO: upgrade
class EventDistributor
(targetEventQueue: EventQueue)
  extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case event: Event =>
      log.info(s"event: $event")
      targetEventQueue.enqueue(event)
    case _ =>
      throw new Exception("unexpected event")
  }
}
