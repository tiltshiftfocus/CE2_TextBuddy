/* TextBuddy++ CE2 by Lim Zhen Ming
 * A0111830X
 * 
 * This is CS2103 Assignment CE2, which is a simple text file editor.
 * 
 * This program is assumed to be run and input by the user from the keyboard.
 * The program can also be run from a text file which consists of commands 
 * as required by the program. 
 * 
 * If input is from a text file, the displayed messages may not be exactly
 * as the given example as the commands entered will not be shown.
 * 
 * The text file used is assumed to be small as the implementations might not
 * be efficient for large files.
 * 
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextBuddy {

    private static final int ARRAY_INDEX_OFFSET = 1;
	private static final String ERROR_WRITING_FILE = "Error writing to file";
	private static final String ERROR_READING_FILE = "Error reading file";
	private static final String ERROR_INVALID_INDEX = "The line specified is invalid";
	private static final String ERROR_INVALID_COMMAND = "command %1$s in invalid";

	public static void main(String[] args) {	
	    File currentFile = TextBuddy.openFile(args[0]);
        Scanner sc = new Scanner(System.in);

        TextBuddy.showWelcomeMessage(args[0]);
		TextBuddy.runProgramTillExit(sc,currentFile);
    }

	private static void runProgramTillExit(Scanner sc, File currentFile){
		while(true) {
			System.out.print("command: ");
			String userCommand = sc.nextLine();
			executeCommand(userCommand, currentFile);
		}
	}

    private static void executeCommand(String userCommand, File currentFile) {
        String command = getFirstWord(userCommand);
        
        if(command.equals("add")){
        	add(userCommand, currentFile);
        }else if(command.equals("display")){
        	display(currentFile);
        }else if(command.equals("clear")){
        	clear(currentFile);
        }else if(command.equals("delete")){
        	delete(userCommand,currentFile);
        }else if(command.equals("sort")){
        	sort(currentFile);
        }else if(command.equals("exit")){
        	System.exit(0);
        }else{
			System.out.println(showError(ERROR_INVALID_COMMAND, userCommand));
		}
    }

	private static void showToUser(String text){
		System.out.println(text);
    }
    
    private static String showError(String errorObject, String userCommand){
    	return String.format(errorObject, userCommand);
    }
    
    public static List<String> sort(File currentFile) {
    	List<String> sortedLines = sortFile(currentFile);
    	
    	System.out.println("file " + currentFile.getName() + " sorted alphabetically");
    	
		return sortedLines;
	}
    
    private static List<String> sortFile(File currentFile){
    	List<String> linesToSort = new LinkedList<String>();
    	
    	try{
	    	BufferedReader inputFile = new BufferedReader(new 
					FileReader(currentFile.getName()));
	    	
	    	String line;
			while((line = inputFile.readLine()) != null){
				linesToSort.add(line);
			}
			Collections.sort(linesToSort, new SortIgnoreCase());
			
    	}catch(IOException e){
    		System.out.println(ERROR_READING_FILE);
    	}
    	
    	clearFile(currentFile);
    	iterateAdd(linesToSort, currentFile);
    	
    	return linesToSort;
    }
 
    public static boolean delete(String userCommand, File currentFile){
    	String textLineToRemove = removeFirstWord(userCommand);
    	if(deleteFromFile(textLineToRemove, currentFile)){
    		return true;
    	}
    	return false;
    }


	private static boolean deleteFromFile(String textLineToRemove, File currentFile) {
		
		int indexOfLineToRemove = Integer.parseInt(textLineToRemove)- ARRAY_INDEX_OFFSET; 
		List<String> linesOfStringFromFile = new LinkedList<String>();
		
		// add all Strings from file to LinkedList, store deleted String,
		// and removing the string, then clear current file
		addAllStringToList(currentFile, linesOfStringFromFile);
		
		if(isValidIndex(indexOfLineToRemove,linesOfStringFromFile.size())){
			String deletedString = linesOfStringFromFile.remove(indexOfLineToRemove);
			clearFile(currentFile);
			
			// using Iterator to loop LinkedList 
			// and adding Strings back to currentFile
			iterateAdd(linesOfStringFromFile, currentFile);
			
			System.out.println("deleted from " + currentFile.getName()
					+ ": \"" + deletedString + "\"");
			return true;
		}else{
			System.out.println(ERROR_INVALID_INDEX);
		}
		
		return false;

	}

	private static void addAllStringToList(File currentFile, List<String> linesOfStringFromFile) {
		
		try{
			BufferedReader inputFile = new BufferedReader(new 
					FileReader(currentFile.getName()));
			String line;
			while((line = inputFile.readLine()) != null){
				linesOfStringFromFile.add(line);
			}
		}catch(IOException e){
			System.out.println(ERROR_READING_FILE);
		}
	}
	
	public static boolean clear(File currentFile) {
		if(clearFile(currentFile)){
			System.out.println("all content deleted from " + currentFile.getName());
			return true;
		}else{
			return false;
		}
		
	}

	private static boolean clearFile(File currentFile) {
		try {
			BufferedWriter outToFile = new BufferedWriter(new 
					FileWriter(currentFile.getName(),false));
			
			outToFile.write("");
			outToFile.close();
			return true;
			
		} catch (IOException e) {
			System.out.println(ERROR_WRITING_FILE);
		}
		
		return false;
	}
	
	public static void display(File currentFile){
		displayFile(currentFile);
	}

	private static void displayFile(File currentFile) {
		if(isFileEmpty(currentFile)){
			System.out.println(currentFile.getName() + " is empty");
		}else{
			try {
				BufferedReader inputFile = new BufferedReader(new 
						FileReader(currentFile.getName()));
				
				String line;
				int stringAtLine = 0;
				while((line = inputFile.readLine()) != null){
					stringAtLine++;
					System.out.println(stringAtLine + ". " + line);
				}
				
			} catch (Exception e) {
				System.out.println(ERROR_READING_FILE);
			}
		}
	}
	
	public static boolean add(String userCommand, File currentFile) {
		String textToAdd = removeFirstWord(userCommand);
		
		if(addToFile(textToAdd, currentFile)){
			System.out.println("added to " + currentFile.getName()
					+ ": \"" + textToAdd + "\"");
			
			return true;
		}else{
			return false;
		}
	}

	private static boolean addToFile(String textToAdd, File currentFile) {
		
		try {
			BufferedWriter outToFile = new BufferedWriter(new 
					FileWriter(currentFile.getName(),true));
			
			if(!isFileEmpty(currentFile)){
				outToFile.newLine();	// a new line for next String
			}
			
			outToFile.write(textToAdd);
			outToFile.close();
			return true;
			
		} catch (IOException e) {
			System.out.println(ERROR_WRITING_FILE);
		}
		
		return false;
	}

	public static boolean isFileEmpty(File currentFile) {
		return currentFile.length()<=0;
	}
	
	private static boolean isValidIndex(int i, int size){
		return (i>=0 && i<size);
	}
	
	private static void iterateAdd(List<String> linesOfText, File currentFile) {
		Iterator<String> listIterator = linesOfText.iterator();
		while(listIterator.hasNext()){
			String textToAdd = listIterator.next();
			addToFile(textToAdd, currentFile);
		}
	}

    private static void showWelcomeMessage(String arg) {
        System.out.println("Welcome to TextBuddy++. " + arg + " is ready for use.");
    }

    private static File openFile(String fileName) {
        File file = new File(fileName);

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(ERROR_READING_FILE);
                System.exit(0);
            }
        }
        return file;
    }

    private static String removeFirstWord(String userCommand) {
        return userCommand.replace(getFirstWord(userCommand), "").trim();
    }

    private static String getFirstWord(String userCommand) {
        return userCommand.trim().split("\\s+")[0];
    }
}
