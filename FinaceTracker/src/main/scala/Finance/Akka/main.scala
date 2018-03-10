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
import Finance.Akka.Models.AccountMetadataModels._;
import com.typesafe.config.ConfigFactory;
import Finance.Akka.Utils._;
import util.control.Breaks._;
import java.lang.String.format;

object Main extends App{
	val system = ActorSystem("FinanceTrackerApp");
	val AccountsTrackerWorker = system.actorOf(AccountsTracker.props(), "account-tracker");
	val rootsupervisor = system.actorOf(SupervisorWorker.props(), "account-supervisor");
	val screen = new BankUserInterface();

	private[this] def newAccountCreation(account: String) = AccountsTrackerWorker ! ModifyAccounts(account, "add");
	private[this] def deleteAccount(account: String) = AccountsTrackerWorker ! ModifyAccounts(account, "delete");

	private[this] def showExistingAccounts() = {
		implicit val timeout: Timeout = Timeout(500 millis);
		val future: Future[String] 	= (AccountsTrackerWorker ? "showAccounts").mapTo[String];
		Await.result(future, 500 millis); 	// Blocks until all accounts have been shown
	}

	private[this] def modifyAccount(account: String, amount: Double, reason: String) = {
		implicit val timeout: Timeout = Timeout(500 millis);
		val future: Future[List[String]] = (AccountsTrackerWorker ? "getAccounts").mapTo[List[String]];

		future onComplete {
			case Success(accounts) 	=> 	{
											if(accounts.contains(account)){
												rootsupervisor ! InstructActor(account, Receipt(amount, reason));
											}else{
												println(format("Account: %s is not present", account));
											}
										}
			case Failure(ex) 		=> ex.printStackTrace;
		}
	}

	while(true){
		screen.displayMainView();
		var userInput: Map[String, Any] = screen.receiveUserInput();
		userInput.getOrElse("action", "invalid") match {
			case "create_new_account" 	=> 	this.newAccountCreation(userInput("name").asInstanceOf[String]);
			case "delete_account" 		=> 	this.deleteAccount(userInput("name").asInstanceOf[String]);
			case "show_account" 		=> 	this.showExistingAccounts();
			case "modify_account" 		=> 	this.modifyAccount(userInput("name").asInstanceOf[String], 
																userInput("amount").asInstanceOf[Double], 
																userInput("reason").asInstanceOf[String]
															);
			case "terminate" 			=> 	break;
			case "invalid" 				=> 	Unit;
		}
	}
	Await.ready(system.terminate(), 5 seconds);
}