package net.cherry.stream.application.service

import com.twitter.finagle.Service
import com.twitter.finagle.stream.StreamResponse
import net.cherry.stream.application.infrastructure.EventHandler
import org.jboss.netty.handler.codec.http.HttpRequest

trait StreamService extends Service[HttpRequest, StreamResponse] with EventHandler {

}

object StreamService {
  def apply(): StreamService =
    new StreamServiceImp()
}
