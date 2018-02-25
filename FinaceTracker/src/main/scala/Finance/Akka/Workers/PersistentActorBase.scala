package Finance.Akka.Workers;

import akka.actor.{Actor, ActorRef, ActorLogging, Props};
import akka.persistence._;
import akka.event.Logging;
import Finance.Akka.Models.AccountSummaryModels._;

abstract class PersistentActorBase extends PersistentActor with ActorLogging{
	var state = BankAccountState();
	override def persistenceId: String = self.path.name;
	override def preStart() = log.info("Actor {} starting up", self.path.name);
	override def postStop() = log.info("Actor {} shutting down", self.path.name);

	val loggerActorRef: ActorRef = context.actorOf(LoggerWorkerClass.props(), self.path.name + "_logger");
}