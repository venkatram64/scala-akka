package com.venkat.selection3.stash

import akka.actor.{Actor, ActorSystem, Props, Stash}
import akka.actor.Actor.Receive
import com.venkat.selection3.stash.UserStorage.{Connect, Disconnect, Operation}

case class User(username: String, email: String)

object UserStorage {

  trait DBOperation
  object DBOperation{
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }
  case object Connect
  case object Disconnect
  case class Operation(dBOperation: DBOperation, user: Option[User])
}

class UserStorage extends Actor with Stash{

  override def receive: Receive = disconnected

  def connected: Actor.Receive = {
    case Disconnect =>
      println("User Storage Disconnect from DB")
      context.unbecome()
    case Operation(op, user) =>
      println(s"User Storage receive ${op} to do in user: ${user}")
  }

  def disconnected: Actor.Receive = {
    case Connect =>
      println(s"User Storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }

}

object BecomeHotswap extends App{
  import UserStorage._

  val system = ActorSystem("Hotswap-Become")
  val userStorage = system.actorOf(Props[UserStorage],"userStorage")

  userStorage ! Operation(DBOperation.Create, Some(User("admin","admin@admin.com")))

  userStorage ! Connect

  userStorage ! Disconnect

  Thread.sleep(100)

  system.terminate()
}
