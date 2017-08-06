package com.venkat.section2.three

import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}
import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}

import scala.concurrent.duration._

object Aphrodite {
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}

class Aphrodite extends Actor{
  import Aphrodite._

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    println("Aphrodite preStart hook...")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    println("Aphrodite postStop hook...")
  }

  @scala.throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    println("Aphrodite preRestart hook...")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    println("Aphrodite postRestart hook...")
  }

  override def receive: Receive = {
    case "Resume" =>
      throw ResumeException
    case "Stop" =>
      throw StopException
    case "Restart" =>
      throw RestartException
    case _ =>
      throw new Exception
  }
}

class Hera extends Actor{
  import Aphrodite._

  var childRef: ActorRef = _

  override val supervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 second){
      case ResumeException => Resume
      case RestartException => Restart
      case StopException => Stop
      case _: NumberFormatException => Escalate
    }
  }


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    childRef = context.actorOf(Props[Aphrodite], "Aphrodite")
    Thread.sleep(100)
  }

  override def receive: Receive = {
    case msg =>
      println(s"Hera received ${msg}")
      childRef ! msg
      Thread.sleep(100)
  }
}

object Supervision extends App{

  val system = ActorSystem("Supervisor")
  val hera = system.actorOf(Props[Hera], "hera")

  /*hera ! "Resume"
  Thread.sleep(1000)*/

  /*hera ! "Restart"
  Thread.sleep(1000)*/

  hera ! "Stop"
  Thread.sleep(1000)

  system.terminate()
}
