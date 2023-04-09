package commandparsing;

import java.util.Arrays;

public class Postrouting extends CommandParser{
	
	// Instantiating Postrouting command object
	public Postrouting (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// nat postroute on [host] [old] to [new]
		command = "nat postroute on to";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 1, 2, 5);
		numArguments = 3;
 	}
	
	// Returns Host
	public String getHost() {
		return userCommandArray[3];
	}
	
	// Returns Old IP
	public String getOldIP() {
		return userCommandArray[4];
	}
	
	// Returns New IP
	public String getNewIP() {
		return userCommandArray[6];
	}
}