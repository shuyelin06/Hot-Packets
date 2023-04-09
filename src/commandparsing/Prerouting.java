package commandparsing;

import java.util.Arrays;

public class Prerouting extends CommandParser{
	
	// Instantiating Prerouting command object
	public Prerouting (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// nat preroute [src] to [dest] [new_src]
		command = "nat preroute to";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 1, 3);
		numArguments = 3;
 	}
	
	// Returns Source IP
	public String getSrcIP() {
		return userCommandArray[2];
	}
	
	// Returns Dest IP
	public String getDestIP() {
		return userCommandArray[4];
	}
	
	// New Source
	public String getNewSrc() {
		return userCommandArray[5];
	}
}
