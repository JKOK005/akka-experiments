package com.lightbend.akka.sample.actors

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, OneForOneStrategy, Props}
import com.lightbend.akka.sample.msg.Message;

class SupervisorActor extends Actor{

	override def preStart() = println("Supervisor is starting up ... ");
	override def postStop() = println("Supervisor is shutting down ... ");

	override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries=10){
		case _: Exception => Restart
	}

	val worker = context.actorOf(Props[WorkerActor], "WorkerActor1");

	override def receive: Receive = {
		case Message(msg) => worker ! Message(msg);
	}
}