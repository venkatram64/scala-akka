package com.venkat.section5.cluster

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import com.typesafe.config.ConfigFactory
import com.venkat.section5.commons.{Add, BackendRegistration}

import scala.util.Random


class Frontend extends Actor{

  var backends = IndexedSeq.empty[ActorRef]

  override def receive: Receive = {
    case Add if backends.isEmpty =>
      println("Service unavailable, cluster doesn't have backend node.")
    case addOp: Add =>
      println("Fronend: I'll foward add operation to backend node to handle it.")
      backends(Random.nextInt(backends.size)) forward addOp
    case BackendRegistration if !(backends.contains(sender())) =>
      backends = backends :+ sender()
      context watch(sender())
    case Terminated(a) =>
      backends = backends.filterNot(_ == a)
  }
}

object Frontend {
  private var _frontend: ActorRef = _

  def initiate() = {
    var config = ConfigFactory.load().getConfig("Frontend")

    val system = ActorSystem("ClusterSystem", config)

    _frontend = system.actorOf(Props[Frontend], name = "frontend")
  }

  def getFrontend = _frontend
}
