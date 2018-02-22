package Finance.Akka.Workers;

import akka.actor.{Actor, ActorRef, ActorLogging, Props};
import akka.event.Logging;
import Finance.Akka.Models.Models._;

object LoggerWorkerClass{
	def props(actorId:String): Props = Props(new LoggerWorkerClass(actorId));
}

class LoggerWorkerClass(actorId:String) extends Actor with ActorLogging{
	override def preStart() = log.info("Actor {}-logger starting up", actorId);
	override def postStop() = log.info("Actor {}-logger shutting down", actorId);

	override def receive:Receive = {
		case msg: String => log.info("Current value in {}: {}", actorId, msg);
		case receipt: List[Receipt] => {
			log.info("Begin start of records:");
			receipt.foreach{println};
			log.info("End of records:");
		}
	}
}