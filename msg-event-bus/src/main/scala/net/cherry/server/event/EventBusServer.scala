package net.cherry.server.event

import com.redis._
import net.cherry.server.infrastructure.{EventQueueFetcher, EventQueue}
import akka.actor.{Props, ActorSystem}
import net.cherry.server.event.application.EventDistributor

object EventBusServer extends App {

  val sourceRedis = new RedisClient("localhost", 6379)

  val targetRedis = new RedisClient("localhost", 6380)

  val sourceEventQueue = EventQueue.ofRedis(sourceRedis)

  val targetEventQueue = EventQueue.ofRedis(targetRedis)

  val system = ActorSystem("event-bus-server")

  val eventDistributor = system.actorOf(Props(new EventDistributor(targetEventQueue)), "event-bus-distributor")

  val eventFetcher = EventQueueFetcher(sourceEventQueue, eventDistributor, system)

  eventFetcher.start
}
