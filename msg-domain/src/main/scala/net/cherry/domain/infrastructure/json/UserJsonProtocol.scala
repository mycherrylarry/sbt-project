package net.cherry.domain.infrastructure.json

import spray.json._
import net.cherry.domain.model.user.{User, UserId}
import net.cherry.infrastructure.uuid.{UUID, StatusType}

object UserJsonProtocol extends DefaultJsonProtocol {

  implicit object UserJsonFormat extends RootJsonFormat[User] {
    def write(obj: User) =
      JsObject(
        "identity" -> JsString(obj.id.value.toString()),
        "status" -> JsNumber(obj.status.id),
        "name" -> JsString(obj.name)
      )

    def read(value: JsValue) = value.asJsObject.getFields("identity", "status", "name") match {
      case Seq(JsString(id), JsNumber(status), JsString(name)) =>
        User(id = UserId(UUID(id)), status = StatusType(status.toInt), name = name)
      case _ => deserializationError("User Expected")
    }
  }

}
