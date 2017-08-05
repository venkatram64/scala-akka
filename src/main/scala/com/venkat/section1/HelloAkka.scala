package com.venkat.section1

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive

case class WhowToGreet(who: String)

class Greeter extends Actor{
  override def receive: Receive = {
    case WhowToGreet(who) =>
      println(s"Hello $who")
  }
}

object HelloAkka extends App{
  val system = ActorSystem("Hello-Akka")
  val greeter = system.actorOf(Props[Greeter], "greeter")

  greeter ! WhowToGreet("World!")
}
