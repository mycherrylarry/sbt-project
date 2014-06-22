package net.cherry.domain.infrastructure.json

import spray.json._
import net.cherry.domain.model.event._
import net.cherry.domain.model.conversation.ConversationId
import net.cherry.infrastructure.uuid.UUID

object EventContentJsonProtocol extends DefaultJsonProtocol {

  implicit object ConversationCreateJsonFormat extends RootJsonFormat[ConversationCreateEventContent] {
    def write(obj: ConversationCreateEventContent) =
      JsObject(
        "conversation_name" -> JsString(obj.conversationName)
      )

    def read(value: JsValue) = value.asJsObject.getFields("conversation_name") match {
      case Seq(JsString(conversationName)) =>
        ConversationCreateEventContent(conversationName)
      case _ => deserializationError("ConversationCreateEventContent Expected")
    }
  }

  implicit object ConversationJoinJsonFormat extends RootJsonFormat[ConversationJoinEventContent] {
    def write(obj: ConversationJoinEventContent) =
      JsObject(
        "conversation_id" -> JsString(obj.conversationId.value.toString())
      )

    def read(value: JsValue) = value.asJsObject.getFields("conversation_id") match {
      case Seq(JsString(conversationId)) =>
        ConversationJoinEventContent(ConversationId(UUID(conversationId)))
      case _ => deserializationError("ConversationJoinEventContent Expected")
    }
  }

  implicit object MessageSendJsonFormat extends RootJsonFormat[MessageSendEventContent] {
    def write(obj: MessageSendEventContent) =
      JsObject(
        "conversation_id" -> JsString(obj.conversationId.value.toString()),
        "message" -> JsString(obj.message)
      )

    def read(value: JsValue) = value.asJsObject.getFields("conversation_id", "message") match {
      case Seq(JsString(conversationId), JsString(message)) =>
        MessageSendEventContent(conversationId = ConversationId(UUID(conversationId)), message = message)
      case _ => deserializationError("MessageSendEventContent Expected")
    }
  }
}
