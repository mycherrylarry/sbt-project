package net.cherry.stream.application.service

import com.twitter.concurrent.Broker
import com.twitter.finagle.stream.StreamResponse
import com.twitter.util._
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.{HttpHeaders, DefaultHttpResponse, HttpResponseStatus}
import akka.actor.{Props, ActorRef, Actor, ActorSystem}
import scala.collection.mutable.{HashMap, Set => MSet}
import net.cherry.domain.model.event.Event
import spray.json._
import net.cherry.domain.infrastructure.json.EventJsonProtocol._
import net.cherry.domain.model.user.UserId
import net.cherry.infrastructure.uuid.UUID

class StreamServiceImp
(actorSystem: ActorSystem)
  extends StreamService {

  val brokers: HashMap[UserId, MSet[Broker[ChannelBuffer]]] = HashMap()

  val id = UserId(UUID("key"))

  private case class Subscriber(subscriber: Broker[ChannelBuffer])

  private class ConnectionActor extends Actor {
    def receive = {
      case Subscriber(subscriber) =>
        println("connection actor")
        subscriber.send(copiedBuffer("you've connected to the server \r\n".getBytes())).sync()
        if (brokers.contains(id))
          brokers.get(id).map(_.add(subscriber))
        else
          brokers.put(id, MSet(subscriber))
      case _ =>
        throw new Exception("EXP")
    }
  }

  private class DisconnectionActor extends Actor {
    def receive = {
      case Subscriber(subscriber) =>
        println("disconnection actor")
        if (brokers.contains(id))
          brokers.get(id).map(_.remove(subscriber))
      case _ =>
        throw new Exception("EXP")
    }
  }

  private case class HandleEventActor() extends Actor {
    def receive = {
      case event: Event =>
        brokers.get(id).map {
          brokerSet =>
            brokerSet.foreach {
              broker =>
                val eventJson = event.toJson + "\r\n"
                broker.send(copiedBuffer(eventJson.getBytes())).sync()
            }
        }
    }
  }

  val handleEventActor: ActorRef = actorSystem.actorOf(Props(HandleEventActor), "stream-handle-event-actor")

  private val connectionActor: ActorRef = actorSystem.actorOf(Props(new ConnectionActor), "stream-connection-actor")

  private val disconnectionActor: ActorRef = actorSystem.actorOf(Props(new DisconnectionActor), "stream-disconnection-actor")

  def apply(request: HttpRequest): Future[StreamResponse] = {
    val result = Future {
      val subscriber = new Broker[ChannelBuffer]

      connectionActor ! Subscriber(subscriber)

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
          disconnectionActor ! Subscriber(subscriber)

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
