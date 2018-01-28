package com.lightbend.akka.sample.actors;
import com.lightbend.akka.sample.msg._;
import akka.actor.{Actor, Props};
import akka.event.Logging

object WorkerActor{
	def props() = Props(new WorkerActor());
}

class WorkerActor extends Actor{
	val log 	= Logging(context.system, this)

	override def preRestart(reason: Throwable, message: Option[Any]) = {
		println(String.format("Worker actor is now restarting ... "), self.path.name);
		super.preRestart(reason, message)
	}

	override def postRestart(reason: Throwable) = {
		println(String.format("Work actor {} has completed restart ...", self.path.name));
		super.postRestart(reason);
	}

	override def preStart() = println(String.format("Worker %s is starting up ... ", self.path.name));
	override def postStop() = println(String.format("Worker %s is shutting down ... ", self.path.name));

	override def receive: Receive = {
		case DispMessage(msg) => log.info("Actor: {} received Message: {}", self.path.name, msg)
	}
}