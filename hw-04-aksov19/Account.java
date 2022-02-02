// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;


	/**
	 * Creates an account
	 * @param id Unique int for id
	 * @param balance Starting balance
	 */
	public Account(int id, int balance) {
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}


	/**
	 * Adds balance to account (synchronized)
	 * @param bal Balance to add
	 */
	public void addBalance(int bal){
		synchronized (this) {
			balance += bal;
			transactions++;
		}
	}


	/**
	 * Subtracts balance from account (synchronized)
	 * @param bal Balance to subtract
	 */
	public void subtractBalance(int bal){
		synchronized (this) {
			balance -= bal;
			transactions++;
		}
	}

	@Override
	// used for tests
	public boolean equals(Object a){
		return balance == ((Account) a).balance && transactions == ((Account) a).transactions;
	}

	@Override
	public String toString() {
		return "Id: " + id + ", balance: " + balance + ", transactions: " + transactions;
	}
}
