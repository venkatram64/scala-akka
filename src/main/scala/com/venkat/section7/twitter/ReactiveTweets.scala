package com.venkat.section7.twitter

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import twitter4j.Status
//https://stackoverflow.com/questions/35120082/how-to-get-started-with-akka-streams
//https://blog.scalac.io/2017/04/18/akka-streams-introduction.html
//http://blog.colinbreck.com/akka-streams-a-motivating-example/
//https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html

object ReactiveTweets extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()


  val source = Source.fromIterator(() => TwitterClient.retrieveTweets("#Akka"))

  val normalize = Flow[Status].map{ t =>
    Tweet(Author(t.getUser().getName()), t.getText())
  }

  val sink = Sink.foreach[Tweet](println)

  source.via(normalize).runWith(sink).andThen{
    case _ =>
      actorSystem.terminate()
  }

}
