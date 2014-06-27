package net.cherry.server.infrastructure

import akka.actor._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class EventQueueFetcherImpl
(eventQueue: EventQueue,
 receiver: ActorRef,
 actorSystem: ActorSystem)
  extends EventQueueFetcher {

  val Tick = "tick"

  // TODO: Improve this process
  var cancellable: Cancellable = new Cancellable {
    def isCancelled: Boolean = ???

    def cancel(): Unit = ???
  }

  val fetchActor = actorSystem.actorOf(Props(new Actor {
    def receive = {
      case Tick =>
        eventQueue.dequeue.map {
          eventOpt =>
            eventOpt.map {
              event =>
              // TODO: Logging
                println(s"event fetcher: $event")
                receiver ! event
            }
        }
    }
  }))

  def start: Unit = {
    cancellable = actorSystem.scheduler.schedule(0 milliseconds,
      50 milliseconds, fetchActor, Tick
    )
  }

  def stop: Unit = cancellable.cancel()


}
