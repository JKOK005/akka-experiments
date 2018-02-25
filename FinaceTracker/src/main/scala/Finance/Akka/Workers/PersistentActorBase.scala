package Finance.Akka.Workers;

import akka.actor.{Actor, ActorRef, ActorLogging, Props};
import akka.persistence._;
import akka.event.Logging;
import Finance.Akka.Models.Models._;

abstract class PersistentActorBase extends PersistentActor with ActorLogging{
	var state = BankAccountState();
	override def persistenceId: String = self.path.name;
	override def preStart() = log.info("Actor {} starting up", self.path.name);
	override def postStop() = log.info("Actor {} shutting down", self.path.name);

	val loggerActorRef: ActorRef = context.actorOf(LoggerWorkerClass.props(), self.path.name + "_logger");

	def addState(r:Receipt) = {
		state = state.update(r);
		if(state.receiptCounts()%20 == 0){
			self ! "takeSnapShot";
		}
	}

	override def receiveRecover: Receive = {
		case r: Receipt => addState(r);
		case SnapshotOffer(_, snapshot: BankAccountState) => {
			log.info("Restoration of actor via snapshot");
			state = snapshot;
		}
		case RecoveryCompleted => log.info("Restoration completed for {}", persistenceId)
	}
}