package net.cherry.server.infrastructure

import com.redis.RedisClient
import net.cherry.domain.model.event.Event
import scala.concurrent.Future

trait EventQueue {
  def enqueue(event: Event): Future[Unit]

  def dequeue: Future[Option[Event]]
}

object EventQueue {

  def ofRedis(redisClient: RedisClient): EventQueue = EventQueueOfRedis(redisClient)
}
