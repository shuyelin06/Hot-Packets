package commandparsing;

import java.util.Arrays;

public class Prerouting extends CommandParser{
	
	// Instantiating Prerouting command object
	public Prerouting (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		command = "iptables --table nat --insert PREROUTING --source 0.0.0.0/0 --destination";
		commandArray = command.split(" ", 10);
		
		userCommandArray = userCommand.split(" ", 10);
		
		wordsToCheck = Arrays.asList(0, 1, 2, 3, 4, 5, 7);
		numArguments = 2;
 	}
	
	
	
	// Returns source and destination IPs in an array of Strings;
	// returns null if command is invalid
	public String[] getSrcAndDestIPs() {
		String[] srcAndDestIPs = null;
		// if command is valid
		if (checkValidCommand()) {
			srcAndDestIPs = new String[2];
			srcAndDestIPs[0] = "0.0.0.0/0";
			srcAndDestIPs[1] = userCommandArray[8];
		}	
		
		return srcAndDestIPs;
	}
}
