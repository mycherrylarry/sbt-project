package net.cherry.server.infrastructure

import akka.actor.{ActorSystem, ActorRef}

trait EventQueueFetcher {
  def start

  def stop
}

object EventQueueFetcher {
  def apply(eventQueue: EventQueue,
            receiver: ActorRef,
            actorSystem: ActorSystem): EventQueueFetcher =
    new EventQueueFetcherImpl(eventQueue, receiver, actorSystem)
}


