package Finance.Akka.Utils;
import scala.io.StdIn._;

class BankUserInterface{
	private[this] def newAccountCreation(): Map[String, Any] = {
		val resp = readLine("Key in the name of new account to be created: ");
		Map("action" 	-> "create_new_account", 
			"name" 		-> resp
		);
	}

	private[this] def deleteExistingAccount(): Map[String, Any] = {
		val resp = readLine("Key in the name of existing account to delete: ");
		Map("action" 	-> "delete_account", 
			"name" 		-> resp
		);
	}

	private[this] def showAccountInformation(): Map[String, Any] = {
		Map("action" 	-> "show_account");
	}

	private[this] def modifyAccount(): Map[String, Any] = {
		val respAccount = readLine("Select account to modify: ");

		println("1) Add");
		println("2) Deduct");
		val respOptions = readLine("Select options: ");
		val multiplier: Int = respOptions match{
			case "1" => 1;
			case "2" => -1;
			case _ 	 => 0;
		}
		val amount: Double = readLine("Key in value: ").toDouble;
		val reason: String = readLine("State a reason: ");

		if(multiplier != 0){
			Map("action" 	-> "modify_account", 
				"name" 		-> respAccount,
				"amount" 	-> multiplier *amount,
				"reason" 	-> reason
			);
		}else{
			println("Invalid input");
			Map("action" 	-> "invalid");
		}
	}

	private[this] def terminateApp() = {
		Map("action" 	-> "terminate");
	}

	def clearScreen(spaces: Int) = {
		for(_ <- 0 until spaces){
			println(" ");
		}
	}

	def displayMainView() = {
		println("Please select from the various options");
		println("1) Create a new account");
		println("2) Delete existing account");
		println("3) Show accounts and balance");
		println("4) Modify existing account");
		println("5) Exit app");
	}

	def receiveUserInput(): Map[String, Any] = {
		this.clearScreen(10);
		val input = readLine("Selection -> ");
		input match{
			case "1" => this.newAccountCreation();
			case "2" => this.deleteExistingAccount();
			case "3" => this.showAccountInformation();
			case "4" => this.modifyAccount();
			case "5" => this.terminateApp();
			case _ => {
				println("You have selected an invalid input");
				Map("action" 	-> "invalid");
			}
		}
	}
}