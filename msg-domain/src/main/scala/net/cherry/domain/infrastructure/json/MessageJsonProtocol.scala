package net.cherry.domain.infrastructure.json

import spray.json._
import net.cherry.domain.model.message.{Message, MessageId}
import net.cherry.infrastructure.uuid.StatusType

object MessageJsonProtocol extends DefaultJsonProtocol {

  implicit object MessageJsonFormat extends RootJsonFormat[Message] {
    def write(obj: Message) =
      JsObject(
        "identity" -> JsString(obj.id.value),
        "status" -> JsNumber(obj.status.id),
        "content" -> JsString(new String(obj.content))
      )

    def read(value: JsValue) = value.asJsObject.getFields("identity", "status", "content") match {
      case Seq(JsString(id), JsNumber(status), JsString(content)) =>
        Message(id = MessageId(id), status = StatusType(status.toInt), content = content.getBytes())
      case _ => deserializationError("Message Expected")
    }
  }

}

