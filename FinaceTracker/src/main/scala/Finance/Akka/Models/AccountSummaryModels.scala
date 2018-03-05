package Finance.Akka.Models

object AccountSummaryModels{
	case class Receipt(credit: Double, reason: String = "No reason"){
		def getCredit() = credit;
		def getReason() = reason;
		def getInfo() 	= List(credit, reason);
	}
	
	case class BankAccountState(val totalAmount: Double = 0, val receiptCollection: List[Receipt] = Nil){
		def update(r: Receipt): BankAccountState = {
			copy(
				totalAmount = totalAmount +r.getCredit(),
				r :: receiptCollection
			);
		}
		def receiptCounts() = receiptCollection.length;
		def getTotalAmount() = totalAmount;
		def getReceipts() = receiptCollection;
	}

	case class CreateActor(actorId: String);
	case class InstructActor(actorId: String, command: Any);
	case class Synchronize(tellers: List[String]);
}