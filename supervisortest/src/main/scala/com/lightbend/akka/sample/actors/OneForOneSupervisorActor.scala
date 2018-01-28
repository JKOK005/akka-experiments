package com.lightbend.akka.sample.actors

import akka.actor.{ActorRef, PoisonPill}
import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, OneForOneStrategy, Props}
import com.lightbend.akka.sample.msg._;
import akka.routing.{RoundRobinPool, Broadcast};

class SupervisorActor extends Actor{
	override def preStart() = println("Supervisor is starting up ... ");
	override def postStop() = println("Supervisor is shutting down ... ");

	override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries=10){
		case _: Exception => Restart
	}

	val roundRobinActor: ActorRef = context.actorOf(RoundRobinPool(10).props(Props[WorkerActor]), "roundRobinRouter")

	override def receive: Receive = {
		case DispMessage(msg) => roundRobinActor ! DispMessage(msg);

		case PoisonAllWorkersMessage() => {
			roundRobinActor ! Broadcast(PoisonPill);
		}
	}
}