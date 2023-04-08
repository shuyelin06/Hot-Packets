package graphics.boxes;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;

import commandparsing.CommandParser;
import commandparsing.Prerouting;
import commandparsing.CommandParser.CommandType;
import engine.Settings;
import graphics.Box;

public class CommandBox extends Box {
	
	// Saves the Game Container
	private GameContainer container;
	
	// Create TextField 
	private TextField text;
	private TextField prompt;
	
	private UnicodeFont font;
	
	// Track Input
	private Input input;
		
	// Constructor
	public CommandBox(GameContainer container, Input input) {
		this.container = container;
		this.input = input;
		
		font = getNewFont("Courier New " , 37);
	}

	// Initialize the Text Fields and Font
	@Override
	public void initialize() {
		// Load Font
		try {
			font.loadGlyphs();
		} catch (SlickException e) { e.printStackTrace(); }
		
		// Load TextField
		prompt = new TextField(container, font, 
				(int) (centerX - width / 2),
				(int) (centerY - height / 2),
				(int) (width * 0.035f),
				(int) (height));
		
		text = new TextField(container, font, 
				(int) (centerX - width / 2 + width * 0.035f),
				(int) (centerY - height / 2),
				(int) (width * 0.965f),
				(int) (height));
		
	}
	
	// Creates a New Font for the Command
	public UnicodeFont getNewFont(String fontName , int fontSize){
		UnicodeFont returnFont = new UnicodeFont(new Font(fontName, Font.PLAIN, fontSize));
		returnFont.addAsciiGlyphs();
		returnFont.getEffects().add(new ColorEffect(java.awt.Color.green));
		return returnFont;
	}	
	
	@Override
	protected void mouseClick(float mouseX, float mouseY) { }
	
	// Update
	public void update() {
		if ( input.isKeyPressed(Input.KEY_ENTER) ) {
			System.out.println(text.getText());
			parseCommand(text.getText());
			text.setText("");
			
		}
	}
	
	// Draw Method
	// Draws the Command Prompts
	public void draw(Graphics g) {
		super.draw(g);
		
		g.setColor(Color.green);
		prompt.setBorderColor(Color.transparent);
		prompt.setCursorVisible(false);
		prompt.setText("~:");
		prompt.render(container, g);
		
		g.setColor(Color.white);
		text.setBorderColor(Color.transparent);
		text.render(container, g);
		
	}
	
	// Parses command
	private void parseCommand(String text) {
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
//				else if (type == CommandType.POSTROUTING) {
//					Postrouting psting = new Postrouting(text);
//				}
//				else {
//					Filter filt = new Filter(text);
//				}
		}
	}
	
}