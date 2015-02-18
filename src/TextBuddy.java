/* TextBuddy++ CE2 by Lim Zhen Ming
 * A0111830X
 * 
 * This is CS2103 Assignment CE2, which is a simple text file editor.
 * CE2 contains sort and search functions.
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
    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy++. %1$s is ready for use.";
    private static final String MESSAGE_SORTED = "file %1$s sorted alphabetically";
    private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_CLEARED = "all content deleted from %1$s";
	private static final String ERROR_WRITING_FILE = "Error writing to file";
	private static final String ERROR_READING_FILE = "Error reading file";
	private static final String ERROR_INVALID_INDEX = "The line specified is invalid";
	private static final String ERROR_INVALID_COMMAND = "command %1$s in invalid";
	private static final String ERROR_SORT = "unable to sort, %1$s is empty";
	private static final String ERROR_SEARCH = "nothing to search, %1$s is empty";
	private static final String ERROR_KEYWORD_NOT_FOUND = "no matching of keyword \"%1$s\" found";

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
        }else if(command.equals("search")){
        	search(userCommand, currentFile);
        }else if(command.equals("exit")){
        	System.exit(0);
        }else{
			showToUser(String.format(ERROR_INVALID_COMMAND, userCommand));
		}
    }
    
	private static void showToUser(String text){
		System.out.println(text);
    }
	
	// search callee method
	public static List<String> search(String userCommand, File currentFile) {
		List<String> filteredList = null;
		String keyword = removeFirstWord(userCommand);
		
		// check for empty file and/or missing keyword
		if(!isFileEmpty(currentFile) && !keyword.isEmpty()){
			filteredList = searchFile(keyword, currentFile);
		}else if(!isFileEmpty(currentFile) && keyword.isEmpty()){
			showToUser("missing keyword");
		}else if(isFileEmpty(currentFile)){
			showToUser(String.format(ERROR_SEARCH, currentFile.getName()));
		}
		
		return filteredList;
	}
	
	// actual method performing search
	private static List<String> searchFile(String keyword, File currentFile) {
		List<String> filteredList = new LinkedList<String>();
		
		try{
			BufferedReader inputFile = new BufferedReader(new 
					FileReader(currentFile.getName()));
			
			String line;
			while((line = inputFile.readLine()) != null){
				if(line.toLowerCase().contains(keyword.toLowerCase())){
					filteredList.add(line);
				}
			}
			inputFile.close();
			
			// check if matching keyword has been found
			if(!filteredList.isEmpty()){
				iterateAndDisplay(filteredList, currentFile);
			}else{
				showToUser(String.format(ERROR_KEYWORD_NOT_FOUND, keyword));
				return null;
			}
				
		}catch(IOException e){
			showToUser(ERROR_READING_FILE);
		}
		
		return filteredList;
	}
    
	// sort callee method
    public static List<String> sort(File currentFile) {
    	List<String> sortedLines = null;
    	
    	if(!isFileEmpty(currentFile)){
    		sortedLines = sortFile(currentFile);
	    	showToUser(String.format(MESSAGE_SORTED, currentFile.getName()));
    	}else{
    		showToUser(String.format(ERROR_SORT, currentFile.getName()));
    	}
    	
		return sortedLines;
	}
    
    // actual method performing sorting
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
			inputFile.close();
			
    	}catch(IOException e){
    		showToUser(ERROR_READING_FILE);
    	}
    	
    	clearFile(currentFile);
    	iterateAndAdd(linesToSort, currentFile);
    	
    	return linesToSort;
    }
 
    // delete callee method
    public static boolean delete(String userCommand, File currentFile){
    	String textLineToRemove = removeFirstWord(userCommand);
    	if(deleteFromFile(textLineToRemove, currentFile)){
    		return true;
    	}
    	return false;
    }

    // actual method performing delete from file
	private static boolean deleteFromFile(String textLineToRemove, File currentFile) {
		
		int indexOfLineToRemove = Integer.parseInt(textLineToRemove) - ARRAY_INDEX_OFFSET; 
		
		// add all Strings from file to LinkedList, store deleted String,
		// and removing the string, then clear current file
		List<String> linesOfStringFromFile = addAllStringToList(currentFile); 
		
		if(isValidIndex(indexOfLineToRemove,linesOfStringFromFile.size())){
			String deletedString = linesOfStringFromFile.remove(indexOfLineToRemove);
			clearFile(currentFile);
			
			// using Iterator to loop LinkedList 
			// and adding Strings back to currentFile
			iterateAndAdd(linesOfStringFromFile, currentFile);
			
			showToUser(String.format(MESSAGE_DELETED, currentFile.getName(), deletedString));
			
			return true;
		}else{
			showToUser(ERROR_INVALID_INDEX);
		}
		
		return false;

	}
	
	// clear callee method
	public static boolean clear(File currentFile) {
		if(clearFile(currentFile)){
			showToUser(String.format(MESSAGE_CLEARED, currentFile.getName()));
			return true;
		}else{
			return false;
		}
		
	}
	
	// actual method performing clearing of file
	private static boolean clearFile(File currentFile) {
		try {
			BufferedWriter outToFile = new BufferedWriter(new 
					FileWriter(currentFile.getName(),false));
			
			outToFile.write("");
			outToFile.close();
			return true;
			
		} catch (IOException e) {
			showToUser(ERROR_WRITING_FILE);
		}
		
		return false;
	}
	
	// display callee method
	public static void display(File currentFile){
		displayFile(currentFile);
	}

	// actual method performing display of file
	private static void displayFile(File currentFile) {
		if(isFileEmpty(currentFile)){
			showToUser(String.format(MESSAGE_EMPTY, currentFile.getName()));
		}else{
			try {
				List<String> listOfStrings = addAllStringToList(currentFile);
				iterateAndDisplay(listOfStrings, currentFile);
				
			} catch (Exception e) {
				showToUser(ERROR_READING_FILE);
			}
		}
	}
	
	// add callee method
	public static boolean add(String userCommand, File currentFile) {
		String textToAdd = removeFirstWord(userCommand);
		
		if(addToFile(textToAdd, currentFile)){
			showToUser(String.format(MESSAGE_ADDED, currentFile.getName(), textToAdd));
			return true;
		}else{
			return false;
		}
	}

	// actual method performing adding text to file
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
			showToUser(ERROR_WRITING_FILE);
		}
		
		return false;
	}
	
	// method to check if file is empty
	public static boolean isFileEmpty(File currentFile) {
		return currentFile.length()<=0;
	}
	
	// method to check if delete index is valid
	private static boolean isValidIndex(int i, int size){
		return (i>=0 && i<size);
	}
	
	// method to iterate through list and add lines to file
	private static void iterateAndAdd(List<String> linesOfText, File currentFile) {
		Iterator<String> listIterator = linesOfText.iterator();
		while(listIterator.hasNext()){
			String textToAdd = listIterator.next();
			addToFile(textToAdd, currentFile);
		}
	}
	
	// method to iterate through list and display to user
	private static void iterateAndDisplay(List<String> linesOfText, File currentFile){
		Iterator<String> listIterator = linesOfText.iterator();
		int indexOfLine = 0;
		while(listIterator.hasNext()){
			indexOfLine++;
			showToUser(indexOfLine + ". " + listIterator.next());
		}
	}
	
	private static List<String> addAllStringToList(File currentFile) {
		List<String> linesOfStringFromFile = new LinkedList<String>();
		try{
			BufferedReader inputFile = new BufferedReader(new 
					FileReader(currentFile.getName()));
			String line;
			while((line = inputFile.readLine()) != null){
				linesOfStringFromFile.add(line);
			}
			inputFile.close();
		}catch(IOException e){
			showToUser(ERROR_READING_FILE);
		}
		
		return linesOfStringFromFile;
		
	}

    private static void showWelcomeMessage(String arg) {
        showToUser(String.format(MESSAGE_WELCOME, arg));
    }

    private static File openFile(String fileName) {
        File file = new File(fileName);

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                showToUser(ERROR_READING_FILE);
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
