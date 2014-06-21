package net.cherry.domain.infrastructure.json

import spray.json._
import net.cherry.domain.model.event._
import net.cherry.infrastructure.uuid.{UUID, StatusType}
import net.cherry.domain.model.conversation.ConversationId

object EventJsonProtocol extends DefaultJsonProtocol {

  implicit object EventTargetFormat extends RootJsonFormat[EventTarget] {
    def write(obj: EventTarget) =
      JsObject(
        "type" -> JsNumber(obj.targetType.id),
        "identity" -> JsString(obj.id.toString())
      )

    def read(value: JsValue) = value.asJsObject.getFields("type", "identity") match {
      case Seq(JsNumber(tp), JsString(id)) =>
        // TODO: fix bug
        EventTarget(EventTargetType(tp.toInt), UUID(id))
    }
  }

  implicit object EventJsonFormat extends RootJsonFormat[Event] {
    def write(obj: Event) =
      JsObject(
        "identity" -> JsString(obj.id.value.toString()),
        "status" -> JsNumber(obj.status.id),
        "target" -> JsObject(
          "type" -> JsNumber(obj.target.targetType.id),
          "identity" -> JsString(obj.target.id.toString())
        ),
        "content_type" -> JsNumber(obj.contentType.id),
        // TODO: complement
        "content" -> JsString("content")
      )

    def read(value: JsValue) = value.asJsObject.getFields("identity", "status", "target", "content_type", "content") match {
      case Seq(JsString(id), JsNumber(status), target, JsNumber(contentType), JsString(content)) =>
        Event(
          id = EventId(UUID(id)),
          status = StatusType(status.toInt),
          target = target.convertTo[EventTarget],
          contentType = EventContentType(contentType.toInt),
          content = MessageSendEventContent(content)
        )
      case _ => deserializationError("Event Expected")
    }
  }

}

