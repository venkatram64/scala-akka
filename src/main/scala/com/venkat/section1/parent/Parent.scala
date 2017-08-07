package com.venkat.section1.parent

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}
import com.venkat.section1.child.Child


class Parent(childMaker: ActorRefFactory => ActorRef) extends Actor{

  //val child = context.actorOf(Props[Child])
  val child = childMaker(context)
  var ponged = false

  override def receive: Receive = {
    case "ping" => child ! "ping"
    case "pong" => ponged = true
  }
}
