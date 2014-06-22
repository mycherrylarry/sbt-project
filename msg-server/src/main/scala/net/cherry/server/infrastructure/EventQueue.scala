package net.cherry.server.infrastructure

import com.redis.RedisClient

object EventQueue {

  def ofRedis(redisClient: RedisClient): EventQueueOfRedis = EventQueueOfRedis(redisClient)
}
