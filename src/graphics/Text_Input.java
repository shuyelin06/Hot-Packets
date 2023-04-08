package graphics;

import java.util.ArrayList;

import commandparsing.CommandParser;
import commandparsing.Filter;
import commandparsing.Postrouting;
import commandparsing.Prerouting;
import commandparsing.CommandParser.CommandType;

public class Text_Input {

	ArrayList<String> commands = new ArrayList<String>();
	
	boolean flag = true;
	
	// Add command to list of commands
	public void addCommand(String text) {
		this.commands.add(text);
	}
	
	// Return list of commands
	public ArrayList<String> getCommands(){
		return commands;
	}
	
	// Parses command
	public void parseCommand(String text) {
		CommandParser command = new CommandParser(text);
		
		// get type of command and make object of 
		// appropriate type to parse it
		CommandType type = command.getCommandType();
		if (type != null) {
			if (type == CommandType.PREROUTING) {
				Prerouting prting = new Prerouting(text);
				// print src and dest IPs
				System.out.println(prting.getSrcAndDestIPs());
			}
//			else if (type == CommandType.POSTROUTING) {
//				Postrouting psting = new Postrouting(text);
//			}
//			else {
//				Filter filt = new Filter(text);
//			}
		}
	}
	
}
