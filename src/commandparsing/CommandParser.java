package commandparsing;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {

	String command; // master command that user command will be compared with
	String[] commandArray; // master command, but as an array of Strings
	
	String userCommand; // user command
	String[] userCommandArray; // user command, but as an array of Strings
	
	List<Integer> wordsToCheck; // places in user command array to check
	
	int numArguments;
	
	// For returning the type of a command
	public enum CommandType {
		PREROUTING,
		POSTROUTING,
		FILTER
	}
	// Instantiate with command to parse, as well as instantiating
	// static variables
	public CommandParser(String commandToParse) {
		userCommand = commandToParse;
	}
	
	public CommandType getCommandType() {
		CommandType type = null;
		
		// makes Prerouting, Postrouting, and Filter objects out
		// of the command to check which type of command it is.
		// Returns type of command if it is valid, and returns null
		// if command is invalid.
		Prerouting prting = new Prerouting(userCommand);
		if (prting.checkValidCommand()) type = CommandType.PREROUTING;
		else {
			Postrouting psting = new Postrouting(userCommand);
			if (psting.checkValidCommand()) type = CommandType.POSTROUTING;
			else {
				Filter filt = new Filter(userCommand);
				if (filt.checkValidCommand()) type = CommandType.FILTER;
			}
		}
		
		return type;
		
	}
	
	
	// Returns true if command follows template, false if it doesn't
	public boolean checkValidCommand() {
		// check if user command length is same as template command length
		 if (userCommandArray.length == commandArray.length + numArguments) {
			 // checks if user command matches template command
			 for (int i = 0; i < commandArray.length; i++) {
				 String userCommandWord = userCommandArray[wordsToCheck.get(i)];
				 // if user command doesn't match, return false
				 if (!(userCommandWord.equals(commandArray[i])))
					 return false;
			 }
		 }
		 
		 return true;
	}
}
