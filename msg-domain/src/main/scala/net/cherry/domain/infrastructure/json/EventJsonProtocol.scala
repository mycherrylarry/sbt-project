package net.cherry.domain.infrastructure.json

import spray.json._
import net.cherry.domain.model.event._
import net.cherry.infrastructure.uuid.{UUID, StatusType}
import net.cherry.domain.infrastructure.json.EventContentJsonProtocol._

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
    def write(obj: Event) = {
      val content = obj.content match {
        case ConversationCreateEventContent(_) =>
          obj.content.asInstanceOf[ConversationCreateEventContent].toJson
        case ConversationJoinEventContent(_) =>
          obj.content.asInstanceOf[ConversationJoinEventContent].toJson
        case MessageSendEventContent(_, _) =>
          obj.content.asInstanceOf[MessageSendEventContent].toJson
      }
      JsObject(
        "identity" -> JsString(obj.id.value.toString()),
        "status" -> JsNumber(obj.status.id),
        "target" -> JsObject(
          "type" -> JsNumber(obj.target.targetType.id),
          "identity" -> JsString(obj.target.id.toString())
        ),
        "content_type" -> JsNumber(obj.contentType.id),
        "content" -> content
      )
    }

    def read(value: JsValue) = value.asJsObject.getFields("identity", "status", "target", "content_type", "content") match {
      case Seq(JsString(id), JsNumber(status), target, JsNumber(contentType), content) =>
        Event(
          id = EventId(UUID(id)),
          status = StatusType(status.toInt),
          target = target.convertTo[EventTarget],
          contentType = EventContentType(contentType.toInt),
          content = EventContentType(contentType.toInt) match {
            case EventContentType.CONVERSATION_CREATE =>
              content.convertTo[ConversationCreateEventContent]
            case EventContentType.CONVERSATION_JOIN =>
              content.convertTo[ConversationJoinEventContent]
            case EventContentType.MESSAGE_SEND =>
              content.convertTo[MessageSendEventContent]
          }
        )
      case _ => deserializationError("Event Expected")
    }
  }

}

