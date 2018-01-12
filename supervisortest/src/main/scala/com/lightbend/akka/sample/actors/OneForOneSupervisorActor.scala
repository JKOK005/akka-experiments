package com.lightbend.akka.sample.actors

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, OneForOneStrategy, Props}
import com.lightbend.akka.sample.msg._;

class SupervisorActor extends Actor{

	override def preStart() = println("Supervisor is starting up ... ");
	override def postStop() = println("Supervisor is shutting down ... ");

	override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries=10){
		case _: Exception => Restart
	}

	val worker = context.actorOf(WorkerActor.props("Bob"), "WorkerNamedBob");

	override def receive: Receive = {
		case DispMessage(msg) => worker ! DispMessage(msg);
	}
}