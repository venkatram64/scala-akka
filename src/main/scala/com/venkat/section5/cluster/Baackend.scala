package com.venkat.section5.cluster

import akka.actor.{Actor, ActorSystem, Props, RootActorPath}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import com.typesafe.config.ConfigFactory
import com.venkat.section5.commons.{Add, BackendRegistration}


class Baackend extends Actor{

  val cluster = Cluster(context.system)



  override def preStart(): Unit = {
    super.preStart()
    cluster.subscribe(self, classOf[MemberUp])
  }

  override def postStop(): Unit = {
    super.postStop()
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case Add(num1, num2) =>
      println(s"I'm a backend with path: ${self} and I received and operation.")
    case MemberUp(member) =>
      if(member.hasRole("frontend")){
        context.actorSelection(RootActorPath(member.address) /"user" / "frontend") ! BackendRegistration
      }
  }
}

object Baackend{

  def initiate(port: Int) = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load().getConfig("Backend"))

    val system = ActorSystem("ClusterSystem", config)

    val baackend = system.actorOf(Props[Baackend], name="Backend")
  }

}
