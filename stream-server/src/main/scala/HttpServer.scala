import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http.Http
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import java.net.InetSocketAddress
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.handler.codec.http._

/**
 * This example demonstrates a sophisticated HTTP server that handles exceptions
 * and performs authorization via a shared secret. The exception handling and
 * authorization code are written as Filters, thus isolating these aspects from
 * the main service (here called "Respond") for better code organization.
 */
object HttpServer {

  /**
   * A simple Filter that catches exceptions and converts them to appropriate
   * HTTP responses.
   */
  object HandleExceptions extends SimpleFilter[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest, service: Service[HttpRequest, HttpResponse]) = {

      // `handle` asynchronously handles exceptions.
      service(request) handle {
        case error =>
          val statusCode = error match {
            case _: IllegalArgumentException =>
              FORBIDDEN
            case _ =>
              INTERNAL_SERVER_ERROR
          }
          val errorResponse = new DefaultHttpResponse(HTTP_1_1, statusCode)

          errorResponse
      }
    }
  }

  /**
   * A simple Filter that checks that the request is valid by inspecting the
   * "Authorization" header.
   */
  object Authorize extends SimpleFilter[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest, continue: Service[HttpRequest, HttpResponse]) = {
      continue(request)
    }
  }

  /**
   * The service itself. Simply echos back "hello world"
   */
  object Respond extends Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      val response = new DefaultHttpResponse(HTTP_1_1, OK)
      response.setContent(copiedBuffer("hello word".getBytes))
      Future.value(response)
    }
  }

  def main(args: Array[String]) {
    val handleExceptions = HandleExceptions
    val authorize = Authorize
    val respond = Respond

    // compose the Filters and Service together:
    val myService: Service[HttpRequest, HttpResponse] = handleExceptions andThen authorize andThen respond

    val server: Server = ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(8080))
      .name("httpserver")
      .build(myService)
  }
}