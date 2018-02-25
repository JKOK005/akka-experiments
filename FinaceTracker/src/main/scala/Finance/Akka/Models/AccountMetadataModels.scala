package Finance.Akka.Models;

object AccountMetadataModels{
	case class BankAccountsRecord(val presentState:List[List[String]] = Nil){
		def update(val newState:List[String]){
			copy(newState :: presentState);
		}

		def getCurrentAccounts() = {
			if(x.length == 0) presentState;
			else presentState.last;
		};
	}
}