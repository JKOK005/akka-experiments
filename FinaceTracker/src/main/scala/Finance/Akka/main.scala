package Finance.Akka;

import akka.actor.{ActorSystem, ActorRef, Props};
import Finance.Akka.Workers._;
import Finance.Akka.Models.Models._;
import com.typesafe.config.ConfigFactory;

object Main extends App{
	val system 		= ActorSystem("FinanceTrackerApp");
	val rootsupervisor = system.actorOf(Props[SupervisorWorker], "account-supervisor");

	rootsupervisor ! CreateActor("savings-account");
	rootsupervisor ! InstructActor("savings-account","showValue");
	rootsupervisor ! InstructActor("savings-account","showReceipts");
}