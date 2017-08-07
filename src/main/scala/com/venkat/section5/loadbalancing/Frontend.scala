package com.venkat.section5.loadbalancing

import scala.concurrent.duration._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import com.venkat.section5.commons.Add
import scala.util.Random


class Frontend extends Actor{
  import context.dispatcher

  var backend = context.actorOf(FromConfig.props(),name = "backendRouter")

  context.system.scheduler.schedule(3.seconds, 3.seconds, self,
  Add(Random.nextInt(100),Random.nextInt(100)))

  override def receive: Receive = {

    case addOp: Add =>
      println("Fronend: I'll foward add operation to backend node to handle it.")
      backend forward addOp
  }

}

object Frontend {
  private var _frontend: ActorRef = _

  def initiate() = {
    var config = ConfigFactory.parseString("akka.cluster.roles=[frontend]").
      withFallback(ConfigFactory.load("loadbaalaancer"))

    val system = ActorSystem("ClusterSystem", config)

    system.log.info("Frontend will start when 2 backend members in the cluster.")

    Cluster(system) registerOnMemberUp {
      _frontend = system.actorOf(Props[Frontend], name = "frontend")
    }
  }
  def getFrontend = _frontend

}
