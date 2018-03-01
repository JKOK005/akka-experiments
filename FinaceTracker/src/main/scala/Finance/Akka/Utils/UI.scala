package Finance.Akka.Utils

class BankUserInterface{
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
	}
}