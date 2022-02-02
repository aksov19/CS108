import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int ans = 0;
		HashMap<T, Integer> map = new HashMap<>();

		Iterator<T> it = a.iterator();
		while(it.hasNext()){
			T cur = it.next();
			if(map.containsKey(cur)) {
				map.put(cur, map.get(cur) + 1);
			}
			else{
				map.put(cur, 1);
			}
		}

		it = b.iterator();
		while(it.hasNext()){
			T cur = it.next();
			if(map.containsKey(cur)) {
				map.put(cur, map.get(cur) - 1);
			}
		}

		it = map.keySet().iterator();
		while(it.hasNext()){
			if(map.get(it.next()) == 0){
				ans++;
			}
		}

		return ans; // YOUR CODE HERE
	}
	
}
