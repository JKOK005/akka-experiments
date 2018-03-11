package Finance.Akka.Workers;

import akka.actor.{Props};
import akka.persistence._;
import akka.event.Logging;
import Finance.Akka.Models.AccountMetadataModels._;

object AccountsTracker{
	def props(): Props = Props(new AccountsTracker());
}

class AccountsTracker extends PersistentActorBase{
	var state = BankAccountsRecord();

	def addState(newState: List[String]) = {
		state = state.update(newState);
		if(state.getRecordCounts()%20 == 0){
			self ! "takeSnapShot";
		}
	}

	override def receiveRecover: Receive = {
		case newState: List[String] => addState(newState);
		case SnapshotOffer(_, snapshot: BankAccountsRecord) => {
			log.info("Restoration of actor via snapshot");
			state = snapshot;
		}
		case RecoveryCompleted => log.info("Restoration completed for {}", persistenceId)
	}

	override def receiveCommand: Receive = {
		case newState: List[Any] => {
			newState match {
				case validNewState: List[String] => {
					if(validNewState != state.getCurrentAccounts()){
						log.info("Persisting accounts state: {}", validNewState);
						persist(validNewState)(addState);
					}else{
						log.warning("New state {} not persisted since nothing has changed from the previous state", validNewState);
					}
				}
				case _ => {
					log.warning("Invalid state given {}", newState)
				}
			}
		}

		case ModifyAccounts(account: String, action: String) => {
			val currentAccounts: List[String] = state.getCurrentAccounts();
			if(!currentAccounts.contains(account) && action == "add"){
				val latestAccounts 	= account :: state.getCurrentAccounts();
				self ! latestAccounts;
			}else if(currentAccounts.contains(account) && action == "delete"){
				self ! currentAccounts.filter(_ != account);
			}
		}

		case "getAccounts" 	=> sender() ! state.getCurrentAccounts();

		case "takeSnapShot" => saveSnapshot(state);
		case SaveSnapshotSuccess(metadata) =>
	      println(s"SaveSnapshotSuccess(metadata): metadata=$metadata");
		case SaveSnapshotFailure(metadata, reason) =>
	      println("""SaveSnapshotFailure(metadata, reason): metadata=$metadata, reason=$reason""");
	}	
}