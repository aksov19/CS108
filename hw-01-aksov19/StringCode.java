import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adjacent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if( str.length() == 0 ) return 0;

		int ans = 1;
		int curAns = 1;
		char prev = str.charAt(0);

		for(int i = 1; i < str.length(); i++){
			if( str.charAt(i) == prev ){
				curAns++;
			}
			else{
				prev = str.charAt(i);
				curAns = 1;
			}

			if( curAns > ans ) ans = curAns;
		}

		return ans;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String ans = "";

		for(int i = 0; i < str.length(); i++){
			if( str.charAt(i) >= '0' && str.charAt(i) <= '9' ){
				if( i != str.length() - 1 ){
					for(int j = 0; j < str.charAt(i) - '0'; j++){
						ans += str.charAt(i + 1);
					}
				}
			}
			else{
				ans += str.charAt(i);
			}
		}

		return ans;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if( a.length() < len || b.length() < len ) return false;

		HashSet<String> set_a = new HashSet<>();
		for(int i = 0; i <= a.length() - len; i++){
			String cur = a.substring(i, i+len);
			set_a.add(cur);
		}

		for(int i = 0; i <= b.length() - len; i++){
			String cur = b.substring(i, i+len);
			if( set_a.contains(cur) ){
				return true;
			}
		}

		return false;
	}
}
