package commandparsing;

import java.util.Arrays;

import core.objects.Packet.Protocol;

// iptables insert source [IP] destination [IP] protocol [TCP, UDP] action [ACCEPT, REJECT, DROP]

public class Filter extends CommandParser{
	
	// Instantiating Filter command object
	public Filter (String commandToParse) {
		super(commandToParse);
		userCommand = commandToParse;
		
		// filter on [src] [src] to [dest] [protocol] [rule]
		command = "filter on to";
		commandArray = command.split(" ");
		
		userCommandArray = userCommand.split(" ");
		
		wordsToCheck = Arrays.asList(0, 1, 4);
		numArguments = 5;
 	}
	
	// Source Device (Apply Rule To)
	public String getHostDevice() {
		return userCommandArray[2];
	}
	
	// Returns Source (Rule)
	public String getRuleSource() {
		return userCommandArray[3];
	}
	
	// Returns Destination (Rule)
	public String getRuleDest() {
		return userCommandArray[5];
	}
	
	// Returns Protocol (Rule)
	public String getProtocol() {
		return userCommandArray[6];
	}
	
	// Returns Rule
	public String getRule() {
		return userCommandArray[7];
	}
	
}