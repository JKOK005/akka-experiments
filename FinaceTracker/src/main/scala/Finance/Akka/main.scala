package Finance.Akka;
import scala.concurrent.{Await, Future};
import scala.concurrent.duration._;
import scala.util.{Success, Failure};
import scala.concurrent.ExecutionContext.Implicits.global;
import akka.pattern.ask;
import akka.util.Timeout;
import akka.actor.{ActorSystem, Props};
import Finance.Akka.Workers._;
import Finance.Akka.Models.AccountSummaryModels._;
import com.typesafe.config.ConfigFactory;
import Finance.Akka.Utils._;
import util.control.Breaks._;

object Main extends App{
	val system = ActorSystem("FinanceTrackerApp");
	val AccountsTrackerWorker = system.actorOf(AccountsTracker.props(), "account-tracker");
	val rootsupervisor = system.actorOf(Props[SupervisorWorker], "account-supervisor");
	val screen = new BankUserInterface();

	private[this] def newAccountCreation(newAccount: String) = {
		implicit val timeout: Timeout = Timeout(500 millis);
		val future: Future[List[String]] = (AccountsTrackerWorker ? "getAccounts").mapTo[List[String]];

		future onComplete {
			case Success(value) => println(value);
			case Failure(ex) 	=> ex.printStackTrace;
		}
	}

	while(true){
		screen.displayMainView();
		var userInput: Map[String, Any] = screen.receiveUserInput();
		userInput.getOrElse("action", "invalid") match {
			case "create_new_account" 	=> 	{
												userInput.get("name") match {
													case Some(name) => this.newAccountCreation(name.asInstanceOf[String]);
												};
											};
			case "delete_account" 		=> 
			case "show_account" 		=> 
			case "modify_account" 		=> 
			case "terminate" 			=> break;
			case "invalid" 				=> Unit;
		}
	}

	Await.ready(system.terminate(), 5 seconds);

	// val accountIds 	= List("savings-account", "bills", "SOWTHWWF");
	// AccountsTrackerWorker ! accountIds;
	// AccountsTrackerWorker ! "showAccounts";

	// rootsupervisor ! CreateActor("savings-account");
	// rootsupervisor ! InstructActor("savings-account","showValue");
	// rootsupervisor ! InstructActor("savings-account","showReceipts");
}