package com.venkat.section7

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
//https://www.programcreek.com/scala/akka.stream.scaladsl.FileIO

object ReadStream extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  // read lines from a log file

  val source = FileIO.fromPath(Paths.get("src/main/resources/application.conf"))

  // parse chunks of bytes into lines
  val flow = Framing.delimiter(ByteString(System.lineSeparator()),
    maximumFrameLength = 512,
    allowTruncation = true).map(_.utf8String)

  val sink = Sink.foreach(println)

  source.via(flow).runWith(sink).andThen {
    case _ =>
      actorSystem.terminate()
  }
}

