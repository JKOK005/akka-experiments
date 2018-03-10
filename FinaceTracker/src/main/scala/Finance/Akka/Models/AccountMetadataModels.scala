package Finance.Akka.Models;

object AccountMetadataModels{
	case class BankAccountsRecord(val presentState:List[List[String]] = Nil){
		def update(newState: List[String]) = {
			copy(newState :: presentState);
		}

		def getCurrentAccounts() = {
			if(presentState.length == 0) List();
			else presentState.head;
		};

		def getRecordCounts() = {
			presentState.length;
		}
	}

	case class ModifyAccounts(account: String, action: String);
}