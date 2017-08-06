package com.venkat.section3.router

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive
import Worker._

class MyRouterGroup(routees: List[String]) extends Actor{

  override def receive: Receive = {
    case msg: Work =>{
      println(s"I'm a Router Group and I receive Work message...")
      context.actorSelection(routees(util.Random.nextInt(routees.size))) forward msg
    }
  }
}

object MyRouterGroup extends App{

  val system = ActorSystem("rounter2")

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")
  system.actorOf(Props[Worker], "w4")
  system.actorOf(Props[Worker], "w5")

  val workers: List[String] = List(
    "/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5"
  )

  val routerGroup = system.actorOf(Props(classOf[MyRouterGroup],workers))
  routerGroup ! Work()
  routerGroup ! Work()

  Thread.sleep(100)
  system.terminate()

}
