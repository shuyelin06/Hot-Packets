package commandparsing;

import java.util.Arrays;

public class Prerouting extends CommandParser{
	
	// Instantiating Prerouting command object
	public Prerouting (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// nat preroute on [host] [old] to [new]
		command = "nat preroute on to";
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
