package com.lightbend.akka.actor

import akka.actor._
import akka.persistence._
import com.lightbend.akka.msg._
import akka.event.Logging

// Note: There are 2 methods of persistence - Via snapshot or Event. 
// A snapshot is a collection of events. This is used only for optimization purposes when the actor has too many events to replay. 
// Apart from the above, there isn't much difference between snapshots and event persistence. 

class PersistentActorTest extends PersistentActor{
	override def persistenceId = "sample-actor-1";

	val log = Logging(context.system, this);
	var state = PersistentActorState();
	def updateState(event: Event): Unit = state = state.update(event);
	val receiveRecover: Receive = {
		case evt: Event => {
			log.info("Restoration of actor via Event");
			this.updateState(evt);
		}

		case SnapshotOffer(_, snapshot: PersistentActorState) => {
			log.info("Restoration of actor via Snapshot");
			state = snapshot;
		}
	}

	var getStateSize = () => this.state.getSize();

	val receiveCommand: Receive = {
		case Execute(cmd) => {
			var cmdStringFormated: String = "%s - %s".format(cmd, this.getStateSize());
			persist(Event(cmdStringFormated))(updateState);
			log.info("Persisted message: {}", cmd);
		}
		case takeSnapshot() => {
			log.info("Issued call to take snapshot");
			saveSnapshot(state);
		}
		case SaveSnapshotSuccess(metadata) => println(s"SaveSnapshotSuccess(metadata):metadata=$metadata");
    	case SaveSnapshotFailure(metadata, reason) => println("""SaveSnapshotFailure(metadata, reason):metadata=$metadata, reason=$reason""");
    	case "print" => {
    		println(state.toString());
    	}
    	case "heartbeat" => {
    		println("heartbeat test Ok!");
    	}
    	case "destroy" => {
    		log.warning("Issued command to terminate");
    		throw new Exception("Supervisor issued command to terminate");
    	}
	}
}