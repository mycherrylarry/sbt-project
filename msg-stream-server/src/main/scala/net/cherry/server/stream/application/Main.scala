package net.cherry.server.stream.application

import akka.actor.{Props, ActorSystem, Actor}

sealed trait Received

case class ReceivedA(param1: String, param2: String) extends Received

case class ReceivedB(param1: String) extends Received

case class ReceivedC(param1: String, param2: String, param3: Int) extends Received

case class Completed(msg: String)

class MyActor1 extends Actor {
  def receive = {
    case ReceivedC(msg1, msg2, msg3) =>
      sender ! Completed("myactor 1 completed")
    case _ =>
      println("unknown")
  }
}

class MyActor extends Actor {
  val myActor1 = context.actorOf(Props[MyActor1], "my_actor_1")
  def receive = {
    case ReceivedA(p1, p2) =>
      println("received A: " + p1 + p2)
    case ReceivedB(p) =>
      println("received B: " + p)
      myActor1 ! ReceivedC("msg1", "msg2", 0)
    case Completed(msg) =>
      println(s"completed: $msg")
    case _ =>
      println("unknown")
  }
}

object Main extends App {

  val system = ActorSystem("mySystem")
  val myActor = system.actorOf(Props[MyActor], "myactor")

  myActor ! ReceivedA("ab", "cd")
  while (true) {
    Thread.sleep(1000)
    myActor ! ReceivedB("abc")
  }
}

