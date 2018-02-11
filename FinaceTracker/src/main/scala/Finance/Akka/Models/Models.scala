object Models{
	class Receipt(credit: Double, reason: String = "No reason"){
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
	}
}