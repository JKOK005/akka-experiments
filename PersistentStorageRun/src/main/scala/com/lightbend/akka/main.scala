package com.lightbend.akka

import akka.actor.{ActorSystem, ActorRef, Props};
import com.lightbend.akka.msg._;
import com.lightbend.akka.actor._

object MainProgramme extends App{
	val system: ActorSystem = ActorSystem("PersistentTestAkka");
	val persistentActor: ActorRef = system.actorOf(Props[PersistentActorTest], "Persistent-Actor-1");
	persistentActor ! "print";
}