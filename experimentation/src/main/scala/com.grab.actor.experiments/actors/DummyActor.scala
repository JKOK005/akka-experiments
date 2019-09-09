package com.grab.actor.experiments.actors

import akka.actor.{Actor, PoisonPill, Props}
import akka.pattern.gracefulStop

class DummyActor extends Actor {
  override def receive: Receive = {
    case x: Int => {
      if(x > 0) {
        println("Creating actor")
        val child = context.actorOf(Props(new DummyActor), "new-child")
        child ! x -1
      }
      else
        Nil
    }

//    case "kill" => {
//      context.children.foreach{child => child ! "kill"}
//      println("Actor terminating")
//      self ! PoisonPill
//    }
  }

  override def postStop(): Unit = {
    println("Actor terminating")
  }
}
