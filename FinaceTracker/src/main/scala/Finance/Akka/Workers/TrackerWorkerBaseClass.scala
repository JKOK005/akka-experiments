package Finance.Akka.Workers;

import akka.actor.{Actor, ActorRef, Props};
import akka.persistence._;
import akka.event.Logging;

class TrackerWorkerBaseClass extends Actor{
	val logger = Logging(context.system, this);

}