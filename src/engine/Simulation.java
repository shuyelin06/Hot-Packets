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
import core.objects.Packet.Protocol;
import core.protocols.FilterRule;
import core.protocols.FilterRule.RuleType;
import core.protocols.NatRule;
import core.protocols.NatRule.NatType;
import graphics.Box;
import graphics.MessageBoard;
import graphics.boxes.CommandBox;
import graphics.boxes.EscapeBox;
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
	
	// Convert from IP String to IP Array
	public static int[] ArrayIP(String ip) {
		int[] convertedIP = new int[4];
		String[] split = ip.split("\\.");
		
		convertedIP[0] = Integer.parseInt(split[0]);
		convertedIP[1] = Integer.parseInt(split[1]);
		convertedIP[2] = Integer.parseInt(split[2]);
		convertedIP[3] = Integer.parseInt(split[3]);
		
		return convertedIP;
	}

	// Convert from IP (Array) to String
	public static String IPString(int[] ip) 
		{ return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3]; }
	
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

	// Graphics
	private ArrayList<Box> boxes;
	private MouseBox mouseBox; // MouseBox (Handles Mouse Panning)
	private InfoBox infoBox; // Information Box (Displays Information)
	private SliderBox simulationBox; // Simulation (Settings) Box
	private CommandBox commandBox; // Command Box
	private EscapeBox escapeIcon; // Escape Button

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
		center = new Vector(0, 0);
		
		// Set Simulation Mode
		simulationMode = Mode.Random;
				
		// Initialize Boxes
		boxes = new ArrayList<>();
		
		escapeIcon = new EscapeBox(arg0);
		escapeIcon
			.setX(0)
			.setY(0)
			.setWidth(20)
			.setHeight(20)
			.initialize();		
		
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
			.setY(0.25f * Settings.Screen_Height)
			.setWidth(0.107f * Settings.Screen_Width)
			.setHeight(0.335f * Settings.Screen_Height)
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
		boxes.add(escapeIcon);

		
		
		// Start Tracking Time
		time = System.currentTimeMillis();

//		/*Man-In-The-Middle*/
//		Device man = new Device(0, -30);
//		man.setIP(0, 0, 0, 0);
//		Device center = new Device(0,0);
//		center.setIP(1, 1, 1, 1);
//		Device left = new Device(-30,0);
//		left.setIP(2, 2, 2, 2);
//		Device right = new Device(30, 0);
//		right.setIP(3, 3, 3, 3);
//		
//		left.addConnection(center);
//		center.addConnection(man);
//		center.addConnection(right);
//		new Packet(left, right, Packet.Protocol.TCP);
//		
//		//blocking package from going to man in the middle
//		center.newFilterRule(new FilterRule(RuleType.DROP, left.getIP(),
//				man.getIP(), Packet.Protocol.TCP));

		
		
//		/* Big Web */
//		Device one = new Device(-65, 10);
//		one.setIP(1, 2, 3, 4);
//		Device two = new Device(-25, 35);
//		two.setIP(1, 3, 5, 7);
//		Device three = new Device(0, -10);
//		three.setIP(1, 1, 2, 3);
//		Device four = new Device(30, 5);
//		four.setIP(2, 4, 6, 8);
//		Device five = new Device(50, 30);
//		five.setIP(3, 6, 9, 12);
//		Device six = new Device(0, 30);
//		six.setIP(4, 2, 1, 2);
//		Device seven = new Device(-50,-10);
//		seven.setIP(5, 2, 4, 2);
//  		
//		two.newFilterRule(new FilterRule(FilterRule.RuleType.DROP, one.getIP(), 
//				two.getIP(), Packet.Protocol.TCP));
//		
//		one.addConnection(two);
//		one.addConnection(six);
//		
//		two.addConnection(three);
//		two.addConnection(four);
//		
//		two.addConnection(five);
//		seven.addConnection(five);
//		three.addConnection(five);
//		four.addConnection(five);
//		
//		five.addConnection(one);
//		
//		six.addConnection(three);
//		
//		new Packet(one, two, Packet.Protocol.TCP);
//		
//		new Packet(one, six, Packet.Protocol.TCP);
		
		/* Central Node */
		Device centrality = new Device(0,0);
		centrality.setIP(10, 10, 10, 10);
		
		Device one = new Device(-45,20);
		Device two = new Device(-45,10);
		Device three = new Device(-45,0);
		Device four = new Device(-45,-10);
		Device five = new Device(-45,-20);
		
		one.setPing(true);
		two.setPing(true);
		three.setPing(true);
		four.setPing(true);
		five.setPing(true);
		
		one.addConnection(centrality);
		one.setIP(0, 0, 0, 0);
		two.addConnection(centrality);
		two.setIP(1, 1, 1, 1);
		three.addConnection(centrality);
		three.setIP(2, 2, 2, 2);
		four.addConnection(centrality);
		four.setIP(3, 3, 3, 3);
		five.addConnection(centrality);
		five.setIP(4, 4, 4, 4);
		
		Device output = new Device(30,0);
		output.setIP(5, 5, 5, 5);
		
		Device clientOne = new Device(40,15);
		clientOne.setIP(6, 6, 6, 6);
		Device clientTwo = new Device(40,-15);
		clientTwo.setIP(7, 7, 7, 7);
		
		output.addConnection(clientOne);
		output.addConnection(clientTwo);
		
		Device mitm = new Device(15,0);
		mitm.setIP(8,8,8,8);
		centrality.addConnection(mitm);
		mitm.addConnection(output);
		
		Device badUser = new Device(15,-20);
		badUser.setIP(9,9,9,9);

//		mitm.addConnection(badUser);
		
		mitm.newNATRule(new NatRule(NatType.DNAT, badUser.getIP(), output.getIP()));
		
		
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