package com.venkat.section1.child

import akka.actor.{Actor, ActorRef}


class Child(parent: ActorRef) extends Actor{

  override def receive: Receive = {
    case "ping" => parent ! "pong"
  }
}
