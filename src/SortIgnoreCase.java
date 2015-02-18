/* TextBuddy++ CE2 by Lim Zhen Ming
 * A0111830X
 * 
 * SortIgnoreCase.java
 * 
 * implements a comparator which ignores case for sort function
 * 
 * */

import java.util.Comparator;

public class SortIgnoreCase implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		
		return s1.toLowerCase().compareTo(s2.toLowerCase());
	}

}
