package com.venkat.section3.router

import akka.actor.Actor
import akka.actor.Actor.Receive


class Worker extends Actor{
  import Worker._

  override def receive: Receive = {
    case msg: Work =>
      println(s"I received work message and my ActorRef: ${self}")
  }
}

object Worker {
  case class Work()
}
