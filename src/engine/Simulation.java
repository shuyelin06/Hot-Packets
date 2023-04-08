package engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.gui.TextField;	

import core.Network;
import core.geometry.Vector;
import core.objects.Device;
import core.objects.Packet;
import core.protocols.FilterRule;
import graphics.Box;
import graphics.MessageBoard;
import graphics.Text_Input;
import graphics.boxes.CommandBox;
import graphics.boxes.InfoBox;
import graphics.boxes.MouseBox;
import graphics.boxes.SliderBox;

public class Simulation extends BasicGameState {
	// ID of GameState (IGNORE)
	private int id;
		
	/* Implements Simulation as a Singleton Object */ 
	private static Simulation simulation = null;
	
	// Returns an Instance of Simulation
	public static Simulation getInstance() {
		if ( simulation == null ) {
			simulation = new Simulation(0);
		}
		return simulation;
	}
	
	// Convert from IP (Array) to String
	public static String IPString(int[] ip) 
		{ return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3]; }
	// Convert from IP (Array) to String, Including CIDR
	public static String IPStringCIDR(int[] ip) 
	{ return IPString(ip) + "/" + ip[4]; }
	
	// Convert from Simulation to Screen Coordinates
	public static float Screen(float val) 
		{ return val * Settings.Pixels_Per_Unit; }
	public static float ScreenX(float x) 
		{ return (x - simulation.center.x) * Settings.Pixels_Per_Unit + Settings.Screen_Width / 2; }
	public static float ScreenY(float y) 
		{ return (Settings.Screen_Height * 0.5f) - (y - simulation.center.y) * Settings.Pixels_Per_Unit;}
	
	// Convert from Screen to Simulation Coordinates
	public static float Sim(float val) 
		{ return val / Settings.Pixels_Per_Unit; }
	public static float SimX(float x) 
		{ return (x - Settings.Screen_Width / 2) / Settings.Pixels_Per_Unit + simulation.center.x; }
	public static float SimY(float y) 
		{ return -(y - Settings.Screen_Height / 2) / Settings.Pixels_Per_Unit + simulation.center.y; }
		
	// Returns a New Font for Use
	public static UnicodeFont getNewFont(String fontName , int fontSize){
		UnicodeFont returnFont = new UnicodeFont(new Font(fontName, Font.PLAIN, fontSize));
		returnFont.addAsciiGlyphs();
		returnFont.getEffects().add(new ColorEffect(java.awt.Color.green));
		return returnFont;
	}	
	
	/* Variables for Simulation */
	// Simulation Mode 
	public enum Mode { Random, User }
	private Mode simulationMode;

	// Center of the Simulation
	private Vector center;
	public Vector getCenter() { return center; }

	// Track Time (Milliseconds)
	private long time;

	// Network the Simulation Uses
	private Network network;

	// Track User Input
	private Input input;

	// Track Commands
	private Text_Input commands;

	// Graphics
	private ArrayList<Box> boxes;
	private MouseBox mouseBox; // MouseBox (Handles Mouse Panning)
	private InfoBox infoBox; // Information Box (Displays Information)
	private SliderBox simulationBox; // Simulation (Settings) Box
	private CommandBox commandBox; // Command Box

	// Constructor
	public Simulation(int id) { 
		this.id = id;
	}
	
	@Override
	public int getID() { return id; }

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// Obtain User Input
		input = arg0.getInput();

		// Obtain Network
		network = Network.getInstance();

		// Initialize Center
		center = new Vector(30, 30);
				
		
		commands = new Text_Input();
		
		// Set Simulation Mode
		simulationMode = Mode.Random;
				
		// Initialize Boxes
		boxes = new ArrayList<>();
		
		mouseBox = new MouseBox(input);
		mouseBox
			.setX(Settings.Screen_Width / 2f)
			.setY(Settings.Screen_Height / 2f)
			.setWidth(Settings.Screen_Width)
			.setHeight(Settings.Screen_Height)
			.initialize();
		
		infoBox = new InfoBox(); // Add InfoBox
		infoBox
			.setX(0.896f * Settings.Screen_Width)
			.setY(0.667f * Settings.Screen_Height)
			.setWidth(0.174f * Settings.Screen_Width)
			.setHeight(0.333f * Settings.Screen_Height)
			.initialize();
		
		simulationBox = new SliderBox();
		simulationBox
			.setX(0.931f * Settings.Screen_Width)
			.setY(0.167f * Settings.Screen_Height)
			.setWidth(0.107f * Settings.Screen_Width)
			.setHeight(0.222f * Settings.Screen_Height)
			.initialize();
		
		commandBox = new CommandBox(arg0, input);
		commandBox
			.setX(0.5f * Settings.Screen_Width)
			.setY(0.9f * Settings.Screen_Height)
			.setWidth(0.95f * Settings.Screen_Width)
			.setHeight(0.055f * Settings.Screen_Height)
			.initialize();
		
		boxes.add(mouseBox);
		boxes.add(infoBox);
		boxes.add(simulationBox);
		boxes.add(commandBox);
		
		// Start Tracking Time
		time = System.currentTimeMillis();

		/* Testing */
		Device one = new Device(5, 50);
		one.setIP(1, 2, 3, 4);
		Device two = new Device(45, 75);
		two.setIP(1, 3, 5, 7);
		Device three = new Device(70, 30);
		three.setIP(1, 1, 2, 3);
		Device four = new Device(100, 45);
		four.setIP(2, 4, 6, 8);
		Device five = new Device(120, 70);
		five.setIP(3, 6, 9, 12);
		Device six = new Device(70, 70);
		six.setIP(4, 2, 1, 2);
  		
		two.insertRule(FilterRule.RuleType.DROP, one.getIP(), 32, 
				two.getIP(), 32, Packet.Protocol.TCP);
		
		one.addConnection(two);
		one.addConnection(six);
		
		two.addConnection(three);
		two.addConnection(four);
		
		three.addConnection(five);
		four.addConnection(five);
		
		five.addConnection(one);
		
		six.addConnection(three);
		
		new Packet(one, two, Packet.Protocol.TCP);
		
		new Packet(one, six, Packet.Protocol.TCP);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Render Simulation
		network.draw(g);
		
		// Draw Boxes
		for ( Box b : boxes ) {
			b.draw(g);
		}

	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {	
		/* Process User Keyboard Input */
		// Packet Generating P
		if ( arg0.getInput().isKeyDown(Input.KEY_P) ) {
			network.sendPacket();
		}
					
		/* Process Mouse and Screen Interactivity */
		// Entity Selection
		if ( input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ) {
			infoBox.setSelected(network.searchForObject(
					Simulation.SimX(input.getMouseX()), Simulation.SimY(input.getMouseY())));
		}
		
		// Box Behavior
		if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
			// Obtain Mouse X and Y
			float mouseX = input.getMouseX();
			float mouseY = input.getMouseY();
			
			// Check if a Box will Handle the Input
			boolean handled = false; 
			
			// Iterate through the boxes (first render to last)
			// Check if the boxes will handle the input
			for ( int i = boxes.size() - 1; i >= 0; i-- ) {
				if ( handled ) break;
				if ( boxes.get(i).handleMouse(mouseX, mouseY) ) {
					handled = true;
				}
			}
			
			if ( input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ) {
				infoBox.setSelected(network.searchForObject(
						Simulation.SimX(input.getMouseX()), Simulation.SimY(input.getMouseY())));
			}
			
		}
		
		// Update all Boxes
		for ( Box b : boxes ) {
			b.update();
		}
		
		// Update Loop:
		// Determines the number of ticks that need to be ran to keep 
		// the simulation up to date
		float timeDifference = (System.currentTimeMillis() - time) / 1000f;
		
		if ( timeDifference > 1 / Settings.Ticks_Per_Second ) {
			time = System.currentTimeMillis();
		}
		
		while ( timeDifference > 1 / Settings.Ticks_Per_Second ) {
			network.update();
			
			timeDifference -= 1 / Settings.Ticks_Per_Second;
		}
	}

}