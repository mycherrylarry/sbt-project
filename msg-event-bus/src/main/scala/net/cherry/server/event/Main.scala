package net.cherry.server.event

import com.redis._
import serialization._
import Parse.Implicits._
import spray.json._
import net.cherry.domain.infrastructure.json.ConversationJsonProtocol._
import net.cherry.domain.model.conversation.{ConversationId, Conversation}
import net.cherry.infrastructure.uuid.{UUID, StatusType}

object Main extends App {

  val redis = new RedisClient("localhost", 6379)

  redis.set("1", "ef")

  println(redis.get("1"))

  redis.hmset("hash", Map("field1" -> "1", "field2" -> 2))

  val x = Conversation(ConversationId(UUID.value), StatusType(0), "name").toJson
  println(x.prettyPrint)
  println(x.compactPrint)
  val t = """{"identity":"e6f221f5-9eec-4959-8144-ec13c9acb87a","status":0,"name":"name"}"""
  val c = JsonParser(t).convertTo[Conversation]
  println(c.name)

  //redis.lpush("json", t)

  val y = redis.rpop[String]("json")
  println(y)

}
