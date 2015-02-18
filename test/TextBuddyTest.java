/* TextBuddy++ CE2 by Lim Zhen Ming
 * A0111830X
 * 
 * JUnit Tests
 * 
 * */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class TextBuddyTest {
	
	private static final File file = new File("mytextfile.txt");
	
	@Test
	public void testAdd(){
		assertTrue(TextBuddy.add("add text", file));
	}
	
	@Test
	public void testClear(){
		assertTrue(TextBuddy.clear(file));
	}
	
	@Test
	public void testDelete(){
		TextBuddy.add("add hundreds of ways", file);
		assertTrue(TextBuddy.delete("delete 1", file));
	}
	
	@Test
	public void testDeleteWhenEmpty(){
		TextBuddy.clear(file);
		assertFalse(TextBuddy.delete("delete 1", file));
	}
	
	@Test
	public void testSort(){
		TextBuddy.clear(file);
		TextBuddy.add("add bbb", file);
		TextBuddy.add("add aaa", file);
		TextBuddy.add("add dDd", file);
		TextBuddy.add("add CCC", file);
		
		List<String> list = TextBuddy.sort(file);
		
		assertEquals("[aaa, bbb, CCC, dDd]", list.toString());
	}
	
	@Test
	public void testSortWhenEmpty(){
		TextBuddy.clear(file);
		
		assertNull(TextBuddy.sort(file));
	}

}
