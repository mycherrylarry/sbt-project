package net.cherry.server.stream.application

import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.stream.Stream
import java.net.InetSocketAddress
import net.cherry.stream.application.service.StreamService
import akka.actor.ActorSystem
import net.cherry.server.infrastructure.{EventQueue, EventQueueFetcher}
import com.redis.RedisClient

/**
 * An example of a streaming server using HTTP Chunking. The Stream
 * Codec uses HTTP Chunks and newline delimited items.
 */
object StreamServer {

  def main(args: Array[String]) {

    val system = ActorSystem("stream-server")

    val streamService = StreamService(system)

    val sourceRedis = new RedisClient("localhost", 6380)

    val streamSourceEventQueue = EventQueue.ofRedis(sourceRedis)

    val eventFetcher = EventQueueFetcher(streamSourceEventQueue, streamService.handleEventActor, system)

    eventFetcher.start

    val server: Server = ServerBuilder()
      .codec(Stream())
      .bindTo(new InetSocketAddress(8079))
      .name("streamserver")
      .build(streamService)

  }
}
