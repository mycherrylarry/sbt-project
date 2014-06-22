package net.cherry.server.infrastructure

import com.redis._
import net.cherry.domain.model.event.Event
import scala.concurrent._
import spray.json._
import net.cherry.domain.infrastructure.json.EventJsonProtocol._
import ExecutionContext.Implicits.global
import serialization._

class EventQueueOfRedisImpl
(val redisClient: RedisClient)
  extends EventQueueOfRedis {

  implicit val parseEvent = Parse[Event] {
    e =>
      val json = new String(e, "UTF-8")
      JsonParser(json).convertTo[Event]
  }

  def enqueue(event: Event): Future[Unit] = future {
    println("enqueue")
    redisClient.lpush("event-json", event.toJson)
  }

  def dequeue: Future[Option[Event]] = future {
    println("dequeue")
    redisClient.rpop[Event]("event-json")
  }
}

