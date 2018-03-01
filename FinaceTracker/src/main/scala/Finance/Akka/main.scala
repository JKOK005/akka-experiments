package Finance.Akka;

import akka.actor.{ActorSystem, ActorRef, Props};
import Finance.Akka.Workers._;
import Finance.Akka.Models.AccountSummaryModels._;
import com.typesafe.config.ConfigFactory;
import Finance.Akka.Utils._;

object Main extends App{
	val system = ActorSystem("FinanceTrackerApp");
	val AccountsTrackerWorker = system.actorOf(AccountsTracker.props(), "account-tracker");
	val rootsupervisor = system.actorOf(Props[SupervisorWorker], "account-supervisor");
	val screen = new BankUserInterface();

	var userInput: Any = Nil;
	while(userInput != "terminate"){
		screen.displayMainView();
		userInput = screen.receiveUserInput();
		println(userInput);
	}

	// val accountIds 	= List("savings-account", "bills", "SOWTHWWF");
	// AccountsTrackerWorker ! accountIds;
	// AccountsTrackerWorker ! "showAccounts";

	// rootsupervisor ! CreateActor("savings-account");
	// rootsupervisor ! InstructActor("savings-account","showValue");
	// rootsupervisor ! InstructActor("savings-account","showReceipts");
}