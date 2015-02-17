import static org.junit.Assert.assertEquals;

import java.io.File;

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
		assertEquals(false, TextBuddy.delete("delete 1", file));
	}

}
