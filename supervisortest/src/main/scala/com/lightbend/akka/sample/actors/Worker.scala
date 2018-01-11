package com.lightbend.akka.sample.actors;
import com.lightbend.akka.sample.msg.Message;
import akka.actor.Actor;

class WorkerActor extends Actor{
	override def preRestart(reason: Throwable, message: Option[Any]) = {
		println("Worker actor is now restarting ... ");
		super.preRestart(reason, message)
	}

	override def postRestart(reason: Throwable) = {
		println("Work actor has completed restart ...");
		super.postRestart(reason);
	}

	override def receive: Receive = {
		case Message(msg) => println(msg)
	}
}