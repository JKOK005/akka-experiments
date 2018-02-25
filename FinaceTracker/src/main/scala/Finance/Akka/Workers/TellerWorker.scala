package Finance.Akka.Workers;

import akka.actor.{Props};
import Finance.Akka.Models.Models._;
import akka.persistence._;

object TellerWorker{
	def props(): Props = Props(new TellerWorker());
}

class TellerWorker extends PersistentActorBase{
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