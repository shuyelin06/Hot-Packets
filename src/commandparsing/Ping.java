package commandparsing;

import java.util.Arrays;

public class Ping extends CommandParser{
	
	// Instantiating Ping command object
	public Ping (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// ping from [source ip]
		command = "ping from";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 1);
		numArguments = 1;
	}
	
	
	// Returns source IP; returns null if command is invalid
	public String getSourceIP() {
		String sourceIP = null;
		// if command is valid
		if (checkValidCommand()) {
			sourceIP = userCommandArray[2];
		}	
		
		return sourceIP;
	}
}