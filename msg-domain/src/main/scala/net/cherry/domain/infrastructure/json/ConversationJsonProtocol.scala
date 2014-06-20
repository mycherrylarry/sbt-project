package net.cherry.domain.infrastructure.json

import spray.json._
import net.cherry.domain.model.conversation.{ConversationId, Conversation}
import net.cherry.infrastructure.uuid.StatusType

object ConversationJsonProtocol extends DefaultJsonProtocol {

  implicit object ConversationJsonFormat extends RootJsonFormat[Conversation] {
    def write(obj: Conversation) =
      JsObject(
        "identity" -> JsString(obj.id.value),
        "status" -> JsNumber(obj.status.id),
        "name" -> JsString(obj.name)
      )

    def read(value: JsValue) = value.asJsObject.getFields("identity", "status", "name") match {
      case Seq(JsString(id), JsNumber(status), JsString(name)) =>
        Conversation(id = ConversationId(id), status = StatusType(status.toInt), name = name)
      case _ => deserializationError("Conversation Expected")
    }
  }

}
