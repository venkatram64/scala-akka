package com.venkat.section3.randomrouter

import akka.actor.{ActorSystem, Props}
import akka.routing.RandomGroup
import com.venkat.section3.router.Worker.Work
import com.venkat.section3.router.{ Worker}


object RandomRounterGroup extends App{
  val system = ActorSystem("Random-Rounter-Group")

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")

  val paths = List("/user/w1","/user/w2", "/user/w3")

  val routerGroup = system.actorOf(RandomGroup(paths).props(),"random-router-group")
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()

  system.terminate()
}
