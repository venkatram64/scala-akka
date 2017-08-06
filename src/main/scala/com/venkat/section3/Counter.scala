package com.venkat.section3

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.actor.Actor.Receive

object Counter {
  final case class Inc(num: Int)
  final case class Dec(num: Int)
}

class Counter extends Actor{
  import Counter._

  var count = 0

  override def receive: Receive = {
    case Inc(x) =>
      count += x
    case Dec(x) =>
      count -= x
  }
}

object CounterTest extends App{

  val system = ActorSystem("Actor-Paths")

  val counter1 = system.actorOf(Props[Counter], "counter")
  println(s"Actor Reference for counter1: ${counter1}")

  val counterSelection1 = system.actorSelection("counter")
  println(s"Actor Selection for counter1: ${counterSelection1}")

  counter1 ! PoisonPill

  Thread.sleep(100)

  val counter2 = system.actorOf(Props[Counter], "Counter")
  println(s"Actor Reference for counter2: ${counter2}")

  val counterSelection2 = system.actorSelection("counter")
  println(s"Actor Selection for counter2: ${counterSelection2}")

  system.terminate()


}

