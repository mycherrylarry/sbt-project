package net.cherry.stream.application.service

import com.twitter.finagle.Service
import com.twitter.finagle.stream.StreamResponse
import org.jboss.netty.handler.codec.http.HttpRequest
import akka.actor.{ActorRef, ActorSystem}

trait StreamService extends Service[HttpRequest, StreamResponse] {
  // Call back actor
  val handleEventActor: ActorRef
}

object StreamService {
  def apply(actorSystem: ActorSystem): StreamService =
    new StreamServiceImp(actorSystem)
}
