import com.twitter.conversions.time._
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.stream.Stream
import com.twitter.util.{Timer, JavaTimer}
import java.net.InetSocketAddress
import scala.util.Random

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
      .bindTo(new InetSocketAddress(8081))
      .name("streamserver")
      .build(streamService)


  }
}
