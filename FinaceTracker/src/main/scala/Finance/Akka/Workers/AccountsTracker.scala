package Finance.Akka.Workers;

import akka.actor.{Props};
import akka.persistence._;
import akka.event.Logging;
import Finance.Akka.Models.Models._;

object AccountsTracker{
	def props(workerId: String): Props = Props(new AccountsTracker(workerId));
}

class AccountsTracker(workerId: String) extends PersistentActorBase{
	override def receiveCommand: Receive = {
		case Receipt(amt: Double, comment: String) => {
			log.info("Persisting receipt value: {}, comment: {}", amt, comment);
			persist(Receipt(amt, comment))(addState);
		}
		case "showValue" 	=> this.loggerActorRef ! state.getTotalAmount().toString;
		case "showReceipts" => this.loggerActorRef ! state.getReceipts();

		case "takeSnapShot" => saveSnapshot(state);
		case SaveSnapshotSuccess(metadata) =>
	      println(s"SaveSnapshotSuccess(metadata): metadata=$metadata");
		case SaveSnapshotFailure(metadata, reason) =>
	      println("""SaveSnapshotFailure(metadata, reason): metadata=$metadata, reason=$reason""");
	}	
}