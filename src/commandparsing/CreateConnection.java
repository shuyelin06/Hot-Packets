package commandparsing;

import java.util.Arrays;

public class CreateConnection extends CommandParser{
	
	// Instantiating CreateConnection command object
	public CreateConnection (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// connect [source] to [destination]
		command = "connect to";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 2);
		numArguments = 2;
 	}
	
	
	
	// Returns source IP; returns null if command is invalid
	public String getSourceIP() {
		System.out.println("@: " + userCommandArray[1]);
		return userCommandArray[1];
	}
	
	// Returns destination IP; returns null if command is invalid
	public String getDestinationIP() {
		System.out.println("3: " + userCommandArray[3]);
		return userCommandArray[3];
	}
}