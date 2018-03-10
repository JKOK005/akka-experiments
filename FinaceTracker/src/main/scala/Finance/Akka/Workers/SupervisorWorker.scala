package Finance.Akka.Workers;

import akka.actor.{ActorRef, OneForOneStrategy, Props, PoisonPill};
import akka.actor.SupervisorStrategy._;
import Finance.Akka.Models.AccountSummaryModels._;
import Finance.Akka.Models.AccountMetadataModels._;
import akka.routing.{Broadcast};
import collection.mutable.HashMap;
import akka.persistence._;

object SupervisorWorker {
	def props() = Props(new SupervisorWorker());
}

class SupervisorWorker extends PersistentActorBase{
	var state = BankAccountsRecord();
	this.createTellersOnStart();

	override def preRestart(reason: Throwable, message: Option[Any]) = {
		println(String.format("Supervisor {} is now restarting ... "), self.path.name);
		super.preRestart(reason, message)
	}
	override def postRestart(reason: Throwable) = {
		println(String.format("Supervisor {} has completed restart ...", self.path.name));
		super.postRestart(reason);
		this.createTellersOnStart();
	}

	override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries =5){
		case _: Exception => Escalate;
	}

	private[this] def createTellersOnStart() = {
		for (eachAccount <- state.getCurrentAccounts()) {
			self ! CreateActor(eachAccount);
		}
	}

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
		case CreateActor(actorId: String) => {
			log.info("Creating actor ID: {}", actorId);
			context.child(actorId) match {
				case Some(referredActor) => log.warning("Actor {} alread exists", actorId);
				case None => val newActor: ActorRef = context.actorOf(TellerWorker.props(), actorId);;
			}
		}

		case DeleteActor(actorId: String) => {
			log.info("Killing actor {}", actorId);
			context.child(actorId) match {
				case Some(referredActor) => referredActor ! PoisonPill;
				case None => log.warning("Actor {} does not yet exist", actorId);
			}
		}

		case InstructActor(actorId: String, command: Any) => { 
			context.child(actorId) match {
				case Some(referredActor) => referredActor ! command;
				case None => log.warning("Actor {} does not yet exist", actorId);
			}
		}

		case Synchronize(tellers: List[String]) => {
			val currentTellers: List[String] 		= state.getCurrentAccounts();
			if(currentTellers != tellers){
				val tellersToCreate: List[String] 	= tellers diff currentTellers;
				val tellersToDelete: List[String] 	= currentTellers diff tellers;
				for(eachTellerToCreate <- tellersToCreate) self ! CreateActor(eachTellerToCreate);
				for(eachTellerToDelete <- tellersToDelete) self ! DeleteActor(eachTellerToDelete);
				persist(tellers)(addState);
			}else{
				log.info("Received list {} is a match to {}. Nothing to synchronize.", tellers, currentTellers);
			}
		}			
	}
}