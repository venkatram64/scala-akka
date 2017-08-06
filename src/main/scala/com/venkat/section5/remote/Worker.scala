package com.venkat.section5.remote

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive
import com.typesafe.config.ConfigFactory

class Worker extends Actor {
  import Worker._

  override def receive: Receive = {
    case msg: Work =>
      println(s"I received work message and my ActorRef: ${self}")
  }
}

object Worker {
  case class Work(message: String)
}

object MembersService extends App{
  val config = ConfigFactory.load.getConfig("MembersService")

  val system = ActorSystem("MembersService", config)

  val worker = system.actorOf(Props[Worker],"remote-worker")

  println(s"Worker actor path is ${worker.path}")

  //system.terminate()
}


object MemberServiceLookup extends App{
  val config = ConfigFactory.load.getConfig("MemberServiceLookup")

  val system = ActorSystem("MemberServiceLookup", config)

  val worker = system.actorSelection("akka.tcp://MembersService@127.0.0.1:2552/user/remote-worker")

  worker ! Worker.Work("Hi Remote Actor")

  //system.terminate()
}

object MembersServiceRemoteCreation extends App{
  val config = ConfigFactory.load.getConfig("MembersServiceRemoteCreation")

  val system = ActorSystem("MembersServiceRemoteCreation", config)

  val worker = system.actorOf(Props[Worker],"workerActorRemote")

  println(s"The remote path of worker Actor is ${worker.path}")

  worker ! Worker.Work("Hi Remote Actor")

  //system.terminate()
}

