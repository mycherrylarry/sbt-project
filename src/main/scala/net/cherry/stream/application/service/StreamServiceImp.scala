package net.cherry.stream.application.service

import com.twitter.concurrent.{Broker, Offer}
import com.twitter.finagle.stream.StreamResponse
import com.twitter.util._
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.{HttpHeaders, DefaultHttpResponse, HttpResponseStatus}

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

  // callback function
  def handleEvent(event: String): Offer[Unit] = {
    messages.send(copiedBuffer((event + " \r\n").getBytes()))
  }

  def apply(request: HttpRequest): Future[StreamResponse] = {
    val result = Future {
      val subscriber = new Broker[ChannelBuffer]

      addBroker ! subscriber

      // \r\n is needed when sending message
      subscriber.send(copiedBuffer("you've connected to the server \r\n".getBytes())).sync()

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
