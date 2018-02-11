package Finance.Akka.Workers;

import akka.actor.{Actor, ActorRef, ActorLogging, Props};
import akka.persistence._;
import akka.event.Logging;
import Finance.Akka.Models.Models._;

object TrackerWorkerBaseClass{
	def props(workerId: String): Props = Props(new TrackerWorkerBaseClass(workerId));
}

class TrackerWorkerBaseClass(workerId: String) extends PersistentActor with ActorLogging{
	var state = BankAccountState();
	override def persistenceId: String = workerId;

	override def preStart() = log.info("Actor {} starting up", self.path.name);
	override def postStop() = log.info("Actor {} shutting down", self.path.name);

	def addState(r:Receipt) = {
		state = state.update(r);
		if(state.receiptCounts()%20 == 0){
			self ! "takeSnapShot";
		}
	}

	override def receiveRecover: Receive = {
		case r: Receipt => addState(r);
		case SnapshotOffer(_, snapshot: BankAccountState) => {
			log.info("Restoration of actor via snapshot")
			state = snapshot;
		}
		case RecoveryCompleted => log.info("Restoration completed for {}", persistenceId)
	}

	override def receiveCommand: Receive = {
		case Receipt(amt: Double, comment: String) => {
			log.info("Persisting receipt value: {}, comment: {}", amt, comment);
			persist(Receipt(amt, comment))(addState);
		}
		case "showValue" => sender() ! state.getTotalAmount();
		case "showReceipts" => sender() ! state.getReceipts();

		case "takeSnapShot" => saveSnapshot(state);
		case SaveSnapshotSuccess(metadata) =>
	      println(s"SaveSnapshotSuccess(metadata): metadata=$metadata");
		case SaveSnapshotFailure(metadata, reason) =>
	      println("""SaveSnapshotFailure(metadata, reason): metadata=$metadata, reason=$reason""");
	}	
}