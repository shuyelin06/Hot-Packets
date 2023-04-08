package commandparsing;

import java.util.Arrays;

public class SendPacket extends CommandParser{
	
	// Instantiating SendPacket command object
	public SendPacket (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// send packet source IP [source IP] destination IP [destination IP] protocol [protocol]
		command = "send packet source IP destination IP protocol";
		commandArray = command.split(" ", 10);
		
		userCommandArray = userCommand.split(" ", 10);
		
		wordsToCheck = Arrays.asList(0, 1, 2, 3, 5, 6, 8);
		numArguments = 3;
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

	// Returns destination IP; returns null if command is invalid
	public String getProtocol() {
		String protocol = null;
		// if command is valid
		if (checkValidCommand()) {
			protocol = userCommandArray[9];
		}	
		
		return protocol;
	}
}