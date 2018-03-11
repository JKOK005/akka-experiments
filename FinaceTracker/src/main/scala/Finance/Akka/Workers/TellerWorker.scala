package Finance.Akka.Workers;

import akka.actor.{Props, PoisonPill};
import Finance.Akka.Models.AccountSummaryModels._;
import akka.persistence._;

object TellerWorker{
	def props(): Props = Props(new TellerWorker());
}

class TellerWorker extends PersistentActorBase{
	var state = BankAccountState();

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

	override def receiveCommand: Receive = {
		case Receipt(amt: Double, comment: String) => {
			log.info("Persisting receipt value: {}, comment: {}", amt, comment);
			persist(Receipt(amt, comment))(addState);
		}
		case "showValue" 	=> this.loggerActorRef ! state.getTotalAmount().toString;
		case "showReceipts" => this.loggerActorRef ! state.getReceipts();
		case "kill" 		=> 	{
									val offsetAmt 	= -1 *state.getTotalAmount();
									self ! Receipt(offsetAmt, "Offset balance to 0 due to kill signal for account");
									self ! PoisonPill;
								}

		case "takeSnapShot" => saveSnapshot(state);
		case SaveSnapshotSuccess(metadata) =>
	      println(s"SaveSnapshotSuccess(metadata): metadata=$metadata");
		case SaveSnapshotFailure(metadata, reason) =>
	      println("""SaveSnapshotFailure(metadata, reason): metadata=$metadata, reason=$reason""");
	}	
}