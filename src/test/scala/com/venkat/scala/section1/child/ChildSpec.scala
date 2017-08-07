package com.venkat.scala.section1.child

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.venkat.section1.child.Child
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

/**
  * Created by venkatram.veerareddy on 8/7/2017.
  */
class ChildSpec extends TestKit(ActorSystem("test-system"))
                              with ImplicitSender
                              with FlatSpecLike
                              with BeforeAndAfterAll
                              with MustMatchers {

  override protected def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  "Child Actor" should "send pong message when receive ping message" in {
    val parent = TestProbe()

    val child = system.actorOf(Props(new Child(parent.ref)))

    child ! "ping"

    parent.expectMsg("pong")
  }

}
