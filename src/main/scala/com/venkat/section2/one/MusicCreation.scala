package com.venkat.section2.one

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive
import com.venkat.section2.one.MusicPlayer._
import com.venkat.section2.one.MusicController._


object MusicController{

  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg

  def props = Props[MusicController]

}

class MusicController extends Actor{

  override def receive: Receive = {
    case Play =>
      println("Music Started...")
    case Stop =>
      println("Music Stopped...")
  }

}

object MusicPlayer{

  sealed trait PlayMsg
  case object StopMusic extends PlayMsg
  case object StartMusic extends PlayMsg

}

class MusicPlayer extends Actor{

  override def receive: Receive = {
    case StopMusic =>
      println("I don't want to stop music")
    case StartMusic =>
      val controller = context.actorOf(MusicController.props,"controller")
      controller ! Play
    case _ =>
      println("Unknown Message")
  }

}

object MusicCreation extends App{

  val system = ActorSystem("MusicCreation")

  val player = system.actorOf(Props[MusicPlayer],"player")

  player ! StartMusic

  system.terminate()

}
