package com.lightbend.akka.sample.actors;
import com.lightbend.akka.sample.msg._;
import akka.actor.{Actor, Props};

object WorkerActor{
	def props(name: String) = Props(new WorkerActor(name));
}

class WorkerActor(name: String) extends Actor{
	val myName = name;

	override def preRestart(reason: Throwable, message: Option[Any]) = {
		println("Worker actor is now restarting ... ");
		super.preRestart(reason, message)
	}

	override def postRestart(reason: Throwable) = {
		println("Work actor has completed restart ...");
		super.postRestart(reason);
	}

	override def preStart() = println(String.format("Worker %s is starting up ... ", myName));
	override def postStop() = println(String.format("Worker %s is shutting down ... ", myName));

	override def receive: Receive = {
		case DispMessage(msg) => println(msg)
	}
}