package graphics;

import java.util.ArrayList;

public class Text_Input {

	ArrayList<String> commands = new ArrayList<String>();
	
	boolean flag = true;
	
	public Text_Input() {
	}
	
	public void addCommand(String text) {
		this.commands.add(text);
	}
	
	public ArrayList<String> getCommands(){
		return commands;
	}
	
}
