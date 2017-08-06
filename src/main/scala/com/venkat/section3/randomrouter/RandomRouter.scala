package com.venkat.section3.randomrouter

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import com.venkat.section3.router.Worker
import com.venkat.section3.router.Worker.Work

object RandomRouter extends App{

  val system = ActorSystem("Random-Router")
  val routePool = system.actorOf(FromConfig.props(Props[Worker]), "random-router-pool")

  routePool ! Work()
  routePool ! Work()
  routePool ! Work()
  routePool ! Work()

  Thread.sleep(100)

  system.terminate()
}
