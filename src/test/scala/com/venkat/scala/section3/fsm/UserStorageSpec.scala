package com.venkat.scala.section3.fsm

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestFSMRef, TestKit}
import com.venkat.section3.fsm.UserStorageFSM
import com.venkat.section3.fsm.UserStorageFSM._
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}


class UserStorageSpec extends TestKit(ActorSystem("test-system"))
                            with ImplicitSender
                            with FlatSpecLike
                            with BeforeAndAfterAll
                            with MustMatchers{
  override protected def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  "User Storage" should "Start with diconnected state and empty data" in {
    var storage = TestFSMRef(new UserStorageFSM())

    storage.stateName must equal(Disconnected)
    storage.stateData must equal(EmptyData)
  }

  it should "be connected state if it receive a connect message" in {
    var storage = TestFSMRef(new UserStorageFSM())

    storage ! Connect

    storage.stateName must equal(Connected)
    storage.stateData must equal(EmptyData)
  }

  it should "be still in disconnected state if it receive any other message" in {
    var storage = TestFSMRef(new UserStorageFSM())

    storage ! DBOperation.Create

    storage.stateName must equal(Disconnected)
    storage.stateData must equal(EmptyData)
  }
  it should "be switch to disconnected when if it receive a disconnect message on Connected State" in {
    var storage = TestFSMRef(new UserStorageFSM())

    storage ! Connect

    storage ! Disconnect

    storage.stateName must equal(Disconnected)
    storage.stateData must equal(EmptyData)
  }

  it should "be still on Connected state if it receive any DB operations" in {
    var storage = TestFSMRef(new UserStorageFSM())

    storage ! Connect

    storage ! DBOperation.Create

    storage.stateName must equal(Connected)
    storage.stateData must equal(EmptyData)
  }

}
