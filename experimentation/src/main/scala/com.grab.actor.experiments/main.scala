package com.grab.actor.experiments

import akka.actor.{Actor, ActorSystem, Props}
import actors.DummyActor

object main extends App {
  val system  = ActorSystem("TestSystem")
  val root    = system.actorOf(Props(new DummyActor), "new-child")
  root ! 100000
  Thread.sleep(30000)
  println("Created all actors")
  system.stop(root)
  Thread.sleep(30000)
  println("Killed all actors")
  System.exit(0)
}
