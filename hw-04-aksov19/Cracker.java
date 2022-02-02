// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}



	// used in main and for testing
	public static String encodeString(String arg) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(arg.getBytes());

		byte[] bytes = md.digest();
		return hexToString(bytes);
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length == 0){
			System.out.println("Args: target length [workers]");
			throw new Exception();
		}

		if (args.length == 1){
			try {
				System.out.println(encodeString(args[0]));
			}catch (Exception ignored) { }

			return;
		}

		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		
		// YOUR CODE HERE
		new Cracker(targ, len, num);
	}


	// Used for testing
	private String decodedString;
	public String getDecodedString(){
		return decodedString;
	}


	/**
	 * Starts worker threads, decodes and prints answer
	 * @param targ String to decode
	 * @param len Max length of decoded string
	 * @param num Number of worker threads
	 */
	public Cracker(String targ, int len, int num){
		int charPerThread = CHARS.length / num;
		byte[] ans = hexToArray(targ);
		CountDownLatch latch = new CountDownLatch(num);

		// Start threads
		for(int i = 0; i < num-1; i++){
			Thread t = new Thread(new Worker(i * charPerThread, (i + 1) * charPerThread, len, ans, latch));
			t.start();
		}
		Thread t = new Thread(new Worker((num - 1) * charPerThread, CHARS.length, len, ans, latch));
		t.start();

		// Wait for threads to finish and print answer
		try {
			latch.await();
		} catch (Exception ignored) {}
		System.out.println(decodedString);
		System.out.println("all done");
	}


	private class Worker implements Runnable{
		private final int from;
		private final int to;
		private final int maxLen;
		private final byte[] ans;
		private final CountDownLatch latch;

		public Worker(int from, int to, int maxLen, byte[] ans, CountDownLatch latch){
			this.from = from;
			this.to = to;
			this.maxLen = maxLen;
			this.ans = ans;
			this.latch = latch;
		}

		@Override
		public void run() {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-1");
			} catch (Exception ignored) {}


			byte[] myAns;
			// Put starter chars in queue
			Queue<String> q = new PriorityQueue();
			for(int i = from; i < to; i++){
				q.add(String.valueOf(CHARS[i]));
			}

			while(!q.isEmpty()){
				if(latch.getCount() == 0) return;

				String s = q.poll();
				md.update(s.getBytes());
				myAns = md.digest();

				if(Arrays.equals(ans, myAns)){
					decodedString = s;
					while(latch.getCount() != 0){
						latch.countDown();
					}
					return;
				}

				if(s.length() < maxLen){
					for(char c : CHARS){
						q.add(s + c);
					}
				}
			}

			latch.countDown();
		}
	}
}
