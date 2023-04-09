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
import core.Network;
import core.objects.Device;
import core.objects.Packet;
import commandparsing.CommandParser.CommandType;
import commandparsing.CreateDevice;
import commandparsing.*;
import engine.Settings;
import engine.Simulation;
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
			int srcIP[];
			int destIP[];
			
			Device srcDevice;
			Device destDevice;
			
			try {
			switch ( type ) {
				case PREROUTING:
					Prerouting prting = new Prerouting(text);
					System.out.println("PREROUTING!");
					break;
					
				case POSTROUTING:
					Postrouting psting = new Postrouting(text);
					System.out.println("POSTROUTING!");
					break;
					
				case FILTER:
					Filter filter = new Filter(text);
					System.out.println("FILTER!");
					break;
					
				case CREATEDEVICE:
					CreateDevice createDev = new CreateDevice(text);
					
					int[] ip = Simulation.ArrayIP(createDev.getIP());
					String name = createDev.getName();
					
					Device device = new Device(0, 0);
					device
						.setIP(ip)
						.setName(name);
					
					break;
					
				case CREATECONNECTION:
					CreateConnection createCon = new CreateConnection(text);
					
					// Find IP Addresses
					srcIP = Simulation.ArrayIP(createCon.getSourceIP());
					destIP = Simulation.ArrayIP(createCon.getDestinationIP());
					
					// Find Source and Destination Device
					srcDevice = Network.getInstance().searchForDevice(srcIP);
					destDevice = Network.getInstance().searchForDevice(destIP);
					
					// Attempt to Add Edge
					srcDevice.addConnection(destDevice);
					
					break;
					
				case SENDPACKET:
					SendPacket sendPac = new SendPacket(text);
					
					// Find IP Addresses
					srcIP = Simulation.ArrayIP(sendPac.getSourceIP());
					destIP = Simulation.ArrayIP(sendPac.getDestinationIP());
					
					// Find Source and Destination Device
					srcDevice = Network.getInstance().searchForDevice(srcIP);
					destDevice = Network.getInstance().searchForDevice(destIP);
					
					// Protocol
					Packet.Protocol protocol = 
						( sendPac.getProtocol().toLowerCase().equals("tcp") ? 
								Packet.Protocol.TCP : Packet.Protocol.UDP );
					
					// Randomize Network
					
					// Create a Packet
					new Packet(srcDevice, destDevice, protocol, Network.getInstance());
					
					break;
					
				case PING:
					System.out.println("Ping");
					
					Ping ping = new Ping(text);
					
					// Find IP Address
					srcIP = Simulation.ArrayIP(ping.getSourceIP());
					
					// Find Device
					System.out.println("---");
					for ( int i : srcIP ) { System.out.println(i); }
					Device dev = Network.getInstance().searchForDevice(srcIP);
					// Tell Device to Ping
					dev.setPing(true);
					
					System.out.println(":)");
					System.out.println(dev.getPing());
					
					break;
					
			}
			} catch (Exception e) {}
		}
	}
	
}