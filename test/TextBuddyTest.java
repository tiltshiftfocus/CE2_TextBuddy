/* TextBuddy++ CE2 by Lim Zhen Ming
 * A0111830X
 * 
 * JUnit Tests
 * 
 * */

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class TextBuddyTest {
	
	private static final File file = new File("mytextfile.txt");
	
	@Test
	public void testAdd(){
		assertEquals(true,TextBuddy.add("add text", file));
	}
	
	@Test
	public void testClear(){
		assertEquals(true,TextBuddy.clear(file));
	}
	
	@Test
	public void testDelete(){
		TextBuddy.add("add hundreds of ways", file);
		assertEquals(true,TextBuddy.delete("delete 1", file));
	}
	
	@Test
	public void testDeleteWhenEmpty(){
		TextBuddy.clear(file);
		assertEquals(false, TextBuddy.delete("delete 1", file));
	}
	
	@Test
	public void testSort(){
		TextBuddy.add("add bbb", file);
		TextBuddy.add("add aaa", file);
		TextBuddy.add("add dDd", file);
		TextBuddy.add("add CCC", file);
		
		List<String> list = TextBuddy.sort(file);
		
		assertEquals("[aaa, bbb, CCC, dDd]", list.toString());
	}

}
