package com.venkat.section2.two

import akka.pattern._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actor.Actor.Receive
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import com.venkat.section2.two.Checker._
import com.venkat.section2.two.Recorder._
import com.venkat.section2.two.Storage._

case class User(username: String, email: String)

object Recorder {
  sealed trait RecorderMsg
  case class NewUser(user: User) extends RecorderMsg
  def props(checker: ActorRef, storage: ActorRef) = Props(new Recorder(checker, storage))
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor{

  implicit val timeout = Timeout(5 seconds)

  override def receive: Receive = {
    case NewUser(user) =>
      checker ? CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder: $user in the blacklist")
      }
  }
}

object Checker {
  sealed trait CheckerMsg
  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckerResponse
  case class BlackUser(user: User) extends CheckerMsg
  case class WhiteUser(user: User) extends CheckerMsg

}

class Checker extends Actor{
  val blackList = List(User("Adam", "adam@adam.com"))
  override def receive: Receive = {
    case CheckUser(user) if blackList.contains(user) =>
      println(s"Checker: $user in the blacklist")
      sender() ! BlackUser(user)
    case CheckUser(user) =>
      println(s"Checker: $user not in the blacklist")
      sender() ! WhiteUser(user)
  }
}

object Storage {
  sealed trait StorageMsg
  case class AddUser(user: User) extends StorageMsg

}

class Storage extends Actor {
  var users = List.empty[User]

  override def receive: Receive = {
    case AddUser(user) =>
      println(s"Storage: $user added")
      users = user :: users
  }
}

object TalkToActor extends App{

  val system = ActorSystem("talk-to-actor")

  val checker = system.actorOf(Props[Checker], "checker")

  val storage = system.actorOf(Props[Storage], "storage")

  val recorder = system.actorOf(Recorder.props(checker, storage), "recorder")

  recorder ! Recorder.NewUser(User("Venkatram","Venkatram@mail.com"))

  Thread.sleep(100)

  system.terminate()
}
