package net.cherry.server.infrastructure

import com.redis.RedisClient

object EventQueueOfRedis {
  def apply(redisClient: RedisClient): EventQueue =
    new EventQueueOfRedisImpl(redisClient)
}
