package commandparsing;

import java.util.Arrays;

public class SendPacket extends CommandParser{
	
	// Instantiating SendPacket command object
	public SendPacket (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		// packet [source ip] to [destination ip] [protocol]
		command = "packet to";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 2);
		numArguments = 3;
	}	
	
	
	// Returns source IP; returns null if command is invalid
	public String getSourceIP() {
		String sourceIP = null;
		// if command is valid
		if (checkValidCommand()) {
			sourceIP = userCommandArray[1];
		}	
		
		return sourceIP;
	}
	
	// Returns destination IP; returns null if command is invalid
	public String getDestinationIP() {
		String destinationIP = null;
		// if command is valid
		if (checkValidCommand()) {
			destinationIP = userCommandArray[3];
		}	
		
		return destinationIP;
	}

	// Returns destination IP; returns null if command is invalid
	public String getProtocol() {
		String protocol = null;
		// if command is valid
		if (checkValidCommand()) {
			protocol = userCommandArray[4];
		}	
		
		return protocol;
	}
}