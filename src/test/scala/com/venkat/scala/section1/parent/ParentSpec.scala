package com.venkat.scala.section1.parent

import akka.actor.{ActorRefFactory, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.venkat.section1.parent.Parent
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}


class ParentSpec extends TestKit(ActorSystem("test-system"))
                          with ImplicitSender
                          with FlatSpecLike
                          with BeforeAndAfterAll
                          with MustMatchers{

  override protected def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  "Parent" should "send ping message to child when receive ping it message" in{
    val child = TestProbe()

    val childMaker = (_: ActorRefFactory) => child.ref

    val parent = system.actorOf(Props(new Parent(childMaker)))

    parent ! "ping"

    child.expectMsg("ping")

  }

}
