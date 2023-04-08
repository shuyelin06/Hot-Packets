package commandparsing;

import java.util.Arrays;

public class CreateConnection extends CommandParser{
	
	// Instantiating CreateConnection command object
	public CreateConnection (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// create connection source IP [source IP] destination IP [destination IP]
		command = "create connection source IP destination IP";
		commandArray = command.split(" ", 10);
		
		userCommandArray = userCommand.split(" ", 10);
		
		wordsToCheck = Arrays.asList(0, 1, 2, 3, 5, 6);
		numArguments = 2;
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
	
	// Returns destination IP; returns null if command is invalid
	public String getDestinationIP() {
		String destinationIP = null;
		// if command is valid
		if (checkValidCommand()) {
			destinationIP = userCommandArray[7];
		}	
		
		return destinationIP;
	}
}