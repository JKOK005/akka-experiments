package com.lightbend.akka.sample

import akka.actor.{ActorSystem, ActorRef, Props};
import com.lightbend.akka.sample.msg._;

object MainProgramme extends App {
	import com.lightbend.akka.sample.actors.SupervisorActor;
	val system: ActorSystem = ActorSystem("HelloAkka");
	val rootSupervisor: ActorRef = system.actorOf(Props[SupervisorActor], "DesSupervisor");
	rootSupervisor ! DispMessage("This is a test message");
}