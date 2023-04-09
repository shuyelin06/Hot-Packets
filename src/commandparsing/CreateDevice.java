package commandparsing;

import java.util.Arrays;

public class CreateDevice extends CommandParser{
	
	// Instantiating CreateDevice command object
	public CreateDevice (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// create device [ip]
		command = "create device";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 1);
		numArguments = 1;
 	}
	
	
	
	// Returns IP; returns null if command is invalid
	public String getIP() {
		String ip = null;
		// if command is valid
		if (checkValidCommand()) {
			ip = userCommandArray[2];
		}	
		
		return ip;
	}
	
}