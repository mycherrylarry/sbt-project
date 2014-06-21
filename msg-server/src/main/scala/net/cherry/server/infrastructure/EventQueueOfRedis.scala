package net.cherry.server.infrastructure

import com.redis.RedisClient
import net.cherry.domain.model.event.Event
import scala.concurrent.Future

trait EventQueueOfRedis {
  def enqueue(event: Event): Future[Unit]

  def dequeue: Future[Event]
}

object EventQueueOfRedis {
  def apply(redisClient: RedisClient): EventQueueOfRedis =
    new EventQueueOfRedisImpl(redisClient)
}
