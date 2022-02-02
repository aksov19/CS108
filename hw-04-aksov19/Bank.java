// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts


	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// Use stream tokenizer to get successive words from file
		StreamTokenizer tokenizer = new StreamTokenizer(reader);

		while (true) {
			int read = tokenizer.nextToken();
			if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
			int from = (int)tokenizer.nval;

			tokenizer.nextToken();
			int to = (int)tokenizer.nval;

			tokenizer.nextToken();
			int amount = (int)tokenizer.nval;

			// Insert into blocking queue
			try {
				Transaction t = new Transaction(from, to, amount);
				transactionQueue.put(t);
			}catch(Exception ignored){}
		}
	}



	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) throws Exception {
		// Start threads
		for(int i = 0; i < numWorkers; i++){
			Thread t = new Thread(new Worker(i));
			t.start();
		}

		// Read file using main thread
		readFile(file);

		// Add exit transactions
		for(int i = 0; i < numWorkers; i++){
			try {
				transactionQueue.put(SENTINEL);
			}catch(Exception ignored){}
		}

		// Wait for countdown latch
		try {
			latch.await();
		}catch(Exception ignored){ }

		// Print results
		for(int i = 0; i < ACCOUNTS; i++){
			System.out.println(accounts.get(i));
		}
	}

	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws Exception {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			throw new Exception("Invalid input");
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		// Create the bank object
		new Bank(file, numWorkers);
	}


	private final List<Account> accounts;
	private final ArrayBlockingQueue<Transaction> transactionQueue;
	private final Transaction SENTINEL = new Transaction(-1, -1, -1);
	private final CountDownLatch latch;


	/**
	 * Sets up variables and starts processing the file
	 * @param file File to process
	 * @param numWorkers Max amount of worker threads
	 */
	public Bank(String file, int numWorkers) throws Exception {
		// Initialize accounts list
		accounts = new ArrayList<>(ACCOUNTS);
		for(int i = 0; i < ACCOUNTS; i++){
			accounts.add(new Account(i, 1000));
		}

		// Initialize blocking queue and latch
		transactionQueue = new ArrayBlockingQueue<>(numWorkers);
		latch = new CountDownLatch(numWorkers);

		// Process file
		this.processFile(file, numWorkers);
	}


	private class Worker implements Runnable{
		private final int id;

		public Worker(int id){ this.id = id; }

		@Override
		public void run() {
			while(true){
				try {
					Transaction t = transactionQueue.take();

					if(t.from == -1 && t.to == -1 && t.amount == -1){
						latch.countDown();
						return;
					}

					accounts.get(t.from).subtractBalance(t.amount);
					accounts.get(t.to).addBalance(t.amount);

				}catch(Exception ignored){}
			}
		}
	}

	// used for testing
	public List<Account> getAccounts(){
		return accounts;
	}
}

