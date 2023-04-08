package commandparsing;

import java.util.Arrays;

// iptables insert source [IP] destination [IP] protocol [TCP, UDP] action [ACCEPT, REJECT, DROP]

public class Filter extends CommandParser{
	
	// Instantiating Filter command object
	public Filter (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		command = "iptables insert source destination protocol action";
		commandArray = command.split(" ", 10);
		
		userCommandArray = userCommand.split(" ", 10);
		
		wordsToCheck = Arrays.asList(0, 1, 2, 4, 6, 8);
		numArguments = 4;
 	}
	
	
	
	// Returns source and destination IPs in an array of Strings;
	// returns null if user command is invalid
	public String[] getSrcAndDestIPs() {
		String[] srcAndDestIPs = null;
		// if command is valid
		if (checkValidCommand()) {
			srcAndDestIPs = new String[2];
			srcAndDestIPs[0] = userCommandArray[3];
			srcAndDestIPs[1] = userCommandArray[5];
		}	
		
		return srcAndDestIPs;
	}
	
	// Returns protocol as a String; returns null if user command is invalid 
	public String getProtocol() {
		String protocol = null;
		// if command is valid
		if (checkValidCommand()) {
			protocol = userCommandArray[7];
		}
		
		return protocol;
	}
	
	// Returns action as a String; returns null if user command is invalid 
	public String getAction() {
		String action = null;
		// if command is valid
		if (checkValidCommand()) {
			action = userCommandArray[9];
		}
		
		return action;
	}
}