import com.twitter.concurrent.{Broker, Offer}
import org.jboss.netty.handler.codec.http.{HttpHeaders, DefaultHttpResponse, HttpResponseStatus}
import com.twitter.conversions.time._
import com.twitter.finagle.stream.StreamResponse
import com.twitter.util._
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpRequest, HttpResponseStatus}
import scala.util.{Random}

class StreamServiceImp extends StreamService {
  // "tee" messages across all of the registered brokers.
  val addBroker = new Broker[Broker[ChannelBuffer]]
  val remBroker = new Broker[Broker[ChannelBuffer]]
  val messages = new Broker[ChannelBuffer]

  private[this] def tee(receivers: Set[Broker[ChannelBuffer]]) {
    Offer.select(
      addBroker.recv {
        b => tee(receivers + b)
      },
      remBroker.recv {
        b => tee(receivers - b)
      },
      if (receivers.isEmpty) Offer.never
      else {
        messages.recv {
          m =>
            Future.join(receivers map {
              _ ! m
            } toSeq) ensure tee(receivers)
        }
      }
    )
  }

  // start the process.
  tee(Set())

  def produce(r: Random, t: Timer) {
    t.schedule(1.second.fromNow) {
      val m = copiedBuffer((r.nextInt.toString + "\n").getBytes())
      messages.send(m) andThen produce(r, t)
    }
  }

  //produce(new Random, new JavaTimer)

  // callback function
  def handleEvent(event: String) {
    messages.send(copiedBuffer(event.getBytes()))
  }

  def apply(request: HttpRequest): Future[StreamResponse] = {
    val result = Future {
      val subscriber = new Broker[ChannelBuffer]

      addBroker ! subscriber
      subscriber.send(copiedBuffer("hello ".getBytes())).sync()

      new StreamResponse {
        val httpResponse = {
          val response = new DefaultHttpResponse(
            request.getProtocolVersion(),
            HttpResponseStatus.OK
          )
          response.addHeader(HttpHeaders.Names.CONTENT_TYPE, "text/event-stream; charset=utf-8")
          response.addHeader(HttpHeaders.Names.CACHE_CONTROL, "no-cache")
          response
        }

        def messages = subscriber.recv

        def error = new Broker[Throwable].recv

        def release() = {
          remBroker ! subscriber
          // sink any existing messages, so they
          // don't hold up the upstream.
          subscriber.recv foreach {
            _ => ()
          }
        }
      }
    }
    result
  }
}
