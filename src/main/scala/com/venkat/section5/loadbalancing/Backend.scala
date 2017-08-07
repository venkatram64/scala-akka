package com.venkat.section5.loadbalancing

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.venkat.section5.commons.{Add}


class Backend extends Actor{

  override def receive: Receive = {
    case Add(num1, num2) =>
      println(s"I'm a backend with path: ${self} and I received and operation.")
  }
}

object Backend{

  def initiate(port: Int) = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]")).
      withFallback(ConfigFactory.load("loadbalancer"))

    val system = ActorSystem("ClusterSystem", config)

    val backend = system.actorOf(Props[Backend], name="backend")
  }

}
