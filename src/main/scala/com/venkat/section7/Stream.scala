package com.venkat.section7

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object Stream extends App{

  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  //source
  val input = Source(1 to 10)

  //flow
  val normalize = Flow[Int].map(_ * 2)

  //sink
  val output = Sink.foreach[Int](println)

  input.via(normalize).runWith(output).andThen{
    case _ =>
      actorSystem.terminate()

  }

}
