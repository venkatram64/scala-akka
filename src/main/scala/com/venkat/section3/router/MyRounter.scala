package com.venkat.section3.router

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actor.Actor.Receive
import Worker._


class MyRouter extends Actor{

   var routees: List[ActorRef] = _

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    routees = List.fill(5){
      context.actorOf(Props[Worker])
    }
    println("Rounter preStart: hook...")
  }

  override def receive: Receive = {
    case msg: Work =>
      println("I'm A Router and I recieved a message...")
      routees(util.Random.nextInt(routees.size)) forward msg
  }
}

object MyRouter extends App{

  val system = ActorSystem("router")
  val router = system.actorOf(Props[MyRouter], "myRouter1")

  router ! Work()

  router ! Work()

  router ! Work()

  Thread.sleep(100)

  system.terminate()

}
