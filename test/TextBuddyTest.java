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
	
	// test add method
	@Test
	public void testAdd(){
		assertTrue(TextBuddy.add("add text", file));
	}
	
	// test clear method
	@Test
	public void testClear(){
		// clear file
		TextBuddy.clear(file);
		
		// check if file is actually empty
		assertTrue(TextBuddy.isFileEmpty(file));
		
	}
	
	// test delete method
	@Test
	public void testDelete(){
		TextBuddy.add("add hundreds of ways", file);
		
		assertTrue(TextBuddy.delete("delete 1", file));
	}
	
	// test delete method when file is empty
	@Test
	public void testDeleteWhenEmpty(){
		TextBuddy.clear(file);
		
		assertFalse(TextBuddy.delete("delete 1", file));
	}
	
	// test delete when file is not empty but invalid line
	@Test
	public void testDeleteInvalid(){
		TextBuddy.clear(file);
		TextBuddy.add("add hello", file);
		TextBuddy.add("add cs2103", file);
		
		assertFalse(TextBuddy.delete("delete 3", file));
	}
	
	// test sort method
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
	public void testSort2(){
		TextBuddy.clear(file);
		TextBuddy.add("add hello world", file);
		TextBuddy.add("add hello", file);
		TextBuddy.add("add testing CS2103", file);
		TextBuddy.add("add JUnit", file);
		
		List<String> list = TextBuddy.sort(file);
		
		assertEquals("[hello world, hello, JUnit, testing CS2103]", list.toString());
	}
	
	// test sort when file is empty
	@Test
	public void testSortWhenEmpty(){
		TextBuddy.clear(file);
		
		assertNull(TextBuddy.sort(file));
	}
	
	// test search
	@Test
	public void testSearch(){
		TextBuddy.clear(file);
		TextBuddy.add("add bbb", file);
		TextBuddy.add("add aaa", file);
		TextBuddy.add("add dDd", file);
		TextBuddy.add("add CCC", file);
		
		List<String> filteredList = TextBuddy.search("search bbb", file);
		
		assertEquals("[bbb]", filteredList.toString());
	}
	
	@Test
	public void testSearch2(){
		TextBuddy.clear(file);
		TextBuddy.add("add hello world", file);
		TextBuddy.add("add CS2103", file);
		TextBuddy.add("add TextBuddy", file);
		TextBuddy.add("add SLAP", file);
		
		List<String> filteredList = TextBuddy.search("search Text", file);
		
		assertEquals("[TextBuddy]", filteredList.toString());
	}
	
	// test search when keyword not found
	@Test
	public void testSearchNotFound(){
		TextBuddy.clear(file);
		TextBuddy.add("add hello", file);

		assertNull(TextBuddy.search("search bbb", file));
	}

}
