package net.cherry.stream.application

import com.twitter.conversions.time._
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.stream.Stream
import com.twitter.util.{Timer, JavaTimer}
import java.net.InetSocketAddress
import net.cherry.server.infrastructure.EventPublisher
import net.cherry.stream.application.service.StreamService

/**
 * An example of a streaming server using HTTP Chunking. The Stream
 * Codec uses HTTP Chunks and newline delimited items.
 */
object StreamServer {

  def main(args: Array[String]) {

    val streamService = StreamService()

    val eventPublisher = EventPublisher(streamService)

    val server: Server = ServerBuilder()
      .codec(Stream())
      .bindTo(new InetSocketAddress(8079))
      .name("streamserver")
      .build(streamService)

    val timer = new JavaTimer()
    def test(timer: Timer) {
      timer.schedule(1.second.fromNow) {
        eventPublisher.publish("ping message...") andThen test(timer)
      }
    }

    test(timer)

  }
}
