package commandparsing;

import java.util.Arrays;

public class Ping extends CommandParser{
	
	// Instantiating Ping command object
	public Ping (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// ping from source IP [source IP]
		command = "ping from source IP";
		commandArray = command.split(" ", 10);
		
		userCommandArray = userCommand.split(" ", 10);
		
		wordsToCheck = Arrays.asList(0, 1, 2, 3);
		numArguments = 1;
	}
	
	
	// Returns source IP; returns null if command is invalid
	public String getSourceIP() {
		String sourceIP = null;
		// if command is valid
		if (checkValidCommand()) {
			sourceIP = userCommandArray[4];
		}	
		
		return sourceIP;
	}
}