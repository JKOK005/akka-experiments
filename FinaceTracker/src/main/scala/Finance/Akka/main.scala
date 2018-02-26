package Finance.Akka;

import akka.actor.{ActorSystem, ActorRef, Props};
import Finance.Akka.Workers._;
import Finance.Akka.Models.AccountSummaryModels._;
import com.typesafe.config.ConfigFactory;

object Main extends App{
	val system = ActorSystem("FinanceTrackerApp");
	val AccountsTrackerWorker = system.actorOf(AccountsTracker.props(), "account-tracker");

	val accountIds 	= List("savings-account", "bills", "SOWTHWWF");
	AccountsTrackerWorker ! accountIds;
	AccountsTrackerWorker ! "showAccounts";

	// val rootsupervisor = system.actorOf(Props[SupervisorWorker], "account-supervisor");
	// rootsupervisor ! CreateActor("savings-account");
	// rootsupervisor ! InstructActor("savings-account","showValue");
	// rootsupervisor ! InstructActor("savings-account","showReceipts");
}