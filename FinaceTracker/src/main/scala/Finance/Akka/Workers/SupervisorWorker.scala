package Finance.Akka.Workers;

import akka.actor.{ActorRef, Actor, ActorLogging, OneForOneStrategy, Props, PoisonPill};
import akka.actor.SupervisorStrategy._;
import Finance.Akka.Models.Models._;
import akka.routing.{Broadcast};
import collection.mutable.HashMap;

class SupervisorWorker extends Actor with ActorLogging{
	var subAccountTracker = new HashMap[String, ActorRef];

	override def preStart() 	= log.info("Supervisor {} starting up", self.path.name);
	override def postStop() 	= log.info("Supervisor {} shutting down", self.path.name);
	override def preRestart(reason: Throwable, message: Option[Any]) = {
		println(String.format("Supervisor {} is now restarting ... "), self.path.name);
		super.preRestart(reason, message)
	}
	override def postRestart(reason: Throwable) = {
		println(String.format("Supervisor {} has completed restart ...", self.path.name));
		super.postRestart(reason);
	}

	override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries =5){
		case _: Exception => Escalate;
	}

	override def receive: Receive = {
		case CreateActor(actorId: String) => {
			log.info("Creating actor ID: {}", actorId);
			var newActor: ActorRef = context.actorOf(TrackerWorkerBaseClass.props(actorId), actorId);
			subAccountTracker += (actorId -> newActor);
		}

		case InstructActor(actorId: String, command: String) => {
			if(subAccountTracker.contains(actorId)){
				subAccountTracker.get(actorId) ! command;
			}
		}
	}
}