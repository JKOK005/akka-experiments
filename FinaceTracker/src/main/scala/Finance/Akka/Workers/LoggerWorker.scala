package Finance.Akka.Workers;

import akka.actor.{Actor, ActorRef, ActorLogging, Props};
import akka.event.Logging;
import Finance.Akka.Models.Models._;

object LoggerWorkerClass{
	def props(): Props = Props(new LoggerWorkerClass());
}

class LoggerWorkerClass extends Actor with ActorLogging{
	override def preStart() = log.info("Actor {}-logger starting up", self.path.name);
	override def postStop() = log.info("Actor {}-logger shutting down", self.path.name);

	override def receive:Receive = {
		case msg: String => log.info("Current value in {}: {}", self.path.name, msg);
		case receipt: List[Receipt] => {
			log.info("Begin start of records:");
			receipt.foreach{println};
			log.info("End of records:");
		}
	}
}