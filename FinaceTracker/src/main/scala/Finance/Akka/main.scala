package Finance.Akka;

import akka.actor.{ActorSystem, ActorRef, Props};
import Finance.Akka.Workers._;
import Finance.Akka.Models.Models._;

object Main extends App{
	val system = ActorSystem("FinanceTrackerApp");
	val rootsupervisor = system.actorOf(Props[SupervisorWorker], "account-supervisor");
	rootsupervisor ! CreateActor("savings-account");
	rootsupervisor ! "showValue";
}