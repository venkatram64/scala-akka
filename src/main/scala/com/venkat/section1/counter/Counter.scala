package com.venkat.section1.counter

import akka.actor.Actor
import com.venkat.section1.counter.Counter.{Decrement, GetCount, Increment}

/**
  * Created by venkatram.veerareddy on 8/7/2017.
  */
class Counter extends Actor{

  var count: Int = 0
  override def receive: Receive = {
    case Increment =>
      count += 1
    case Decrement =>
      count -= 1
    case GetCount =>
      sender() ! count
  }

}

object Counter {
  case object Increment
  case object Decrement
  case object GetCount
}
