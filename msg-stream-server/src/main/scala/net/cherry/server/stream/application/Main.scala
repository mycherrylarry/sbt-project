package net.cherry.server.stream.application

import spray.json._
import net.cherry.domain.infrastructure.json.ConversationJsonProtocol._
import net.cherry.domain.model.conversation.{ConversationId, Conversation}
import net.cherry.infrastructure.uuid.{UUID, StatusType}

object Main extends App {
  val x = Conversation(ConversationId(UUID()), StatusType(0), "name").toJson
  println(x.prettyPrint)
  println(x.compactPrint)
  val t = """{"identity":"e6f221f5-9eec-4959-8144-ec13c9acb87a","status":0,"name":"name"}"""
  val c = JsonParser(t).convertTo[Conversation]
  println(c.name)

}

