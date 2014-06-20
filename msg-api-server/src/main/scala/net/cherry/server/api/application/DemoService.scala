package net.cherry.server.api.application

import akka.actor._
import spray.http._
import HttpMethods._
import spray.can.Http
import scala.collection.mutable.Set
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.ChunkedResponseStart
import akka.util.Timeout
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

/**
 * Created by le.chang on 2014/06/20.
 */
class DemoService
  extends Actor with ActorLogging {

  import context.dispatcher

  lazy val index = HttpResponse(
    entity = HttpEntity("hhhhh")
  )

  val clients: Set[ActorRef] = Set.empty

  def receive = {
    case _: Http.Connected => sender ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      sender ! index

    case HttpRequest(GET, Uri.Path("/send_to_all"), _, _, _) =>
      clients.foreach(c=>
        c ! MessageChunk("send_to_all \r\n")
      )

    case HttpRequest(GET, Uri.Path("/stream"), _, _, _) =>
      clients.add(sender)
      val peer = sender
      context actorOf Props(new Streamer(peer, 25))
  }

  class Streamer(client: ActorRef, count: Int) extends Actor with ActorLogging {
    log.debug("Starting streaming response")

    client ! ChunkedResponseStart(HttpResponse(entity = "hello -- stream \r\n"))

    def receive = {
      case _ =>
        println("hhhhh")
    }
  }

}

