package com.venkat.section3

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSystem, Identify, Props}
import akka.actor.Actor.Receive

/**
  * Created by Venkatram on 8/6/2017.
  */
class Watcher extends Actor{

  //var counterRef: ActorRef = _

  val selection = context.actorSelection("/user/counter")

  selection ! Identify(None)

  override def receive: Receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor Reference for counter is ${ref}")
    case ActorIdentity(_, None) =>
      println("Actor selection for actor does not live")
  }
}

object Watch extends App {

  val system = ActorSystem("Watch-actor-selection")
  val counter = system.actorOf(Props[Counter], "counter")
  val watcher = system.actorOf(Props[Watcher], "watcher")

  Thread.sleep(1000)
  system.terminate()
}
