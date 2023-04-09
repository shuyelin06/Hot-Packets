package commandparsing;

import java.util.Arrays;

public class CreateDevice extends CommandParser{
	
	// Instantiating CreateDevice command object
	public CreateDevice (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// create device ip [ip] name [name]
		command = "create device name";
		commandArray = command.split(" ", 10);
		
		userCommandArray = userCommand.split(" ", 10);
		
		wordsToCheck = Arrays.asList(0, 1, 2, 4);
		numArguments = 2;
 	}
	
	
	
	// Returns IP; returns null if command is invalid
	public String getIP() {
		String ip = null;
		// if command is valid
		if (checkValidCommand()) {
			ip = userCommandArray[3];
		}	
		
		return ip;
	}
	
	// Returns name; returns null if command is invalid
	public String getName() {
		return userCommandArray[5];
	}
}