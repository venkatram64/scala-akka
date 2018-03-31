package com.venkat.section7


import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl._
import akka.util.ByteString
import scala.util.{Failure, Success}

//https://github.com/typesafehub/activator-akka-stream-scala/blob/master/src/main/scala/sample/stream/WritePrimes.scala


object WriteStream extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  // Source
  val source = Source(1 to 10000).filter(isPrime)

  // Sink
  val sink = FileIO.toPath(Paths.get("target/primes.txt"))

  // file output sink
  val fileSink = Flow[Int]
    .map(i => ByteString(i.toString + "\n"))
    .toMat(sink)((_, bytesWritten) => bytesWritten)

  // console output sink
  val consoleSink = Sink.foreach[Int](println)

  val graph = GraphDSL.create(fileSink, consoleSink)((slow, _) => slow) { implicit builder =>
    (slow, console) =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2)) // the splitter - like a Unix tee
      source ~> broadcast ~> slow // connect primes to splitter, and one side to file
      broadcast ~> console // connect other side of splitter to console
      ClosedShape
  }
  val materialized = RunnableGraph.fromGraph(graph).run()

  // ensure the output file is closed and the system shutdown upon completion
  materialized.onComplete {
    case Success(_) =>
      actorSystem.terminate()
    case Failure(e) =>
      println(s"Failure: ${e.getMessage}")
      actorSystem.terminate()
  }

  def isPrime(n: Int): Boolean = {
    if (n <= 1) false
    else if (n == 2) true
    else !(2 to (n - 1)).exists(x => n % x == 0)
  }
}
