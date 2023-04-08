package engine;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import core.Network;
import core.geometry.Vector;
import core.objects.Device;
import core.objects.Packet;
import input.MousePanner;

public class Simulation extends BasicGameState {
	// ID of GameState (IGNORE)
	private int id;
	
	// Simulation Mode 
	public enum Mode { Random, User }
	private Mode simulationMode;
	
	// Center of the Simulation
	static private Vector center;
		
	// Track Time (Milliseconds)
	private long time;
	
	// Network the Simulation Uses
	private Network network;
	
	// Track User Input
	private Input input;
	
	// Tracks Mouse Panning
	private MousePanner mousePanner;
	
	// Constructor
	public Simulation(int id) { 
		this.id = id;
	}
	
	// Convert from Simulation to Screen Coordinates
	public static float Screen(float val) 
		{ return val * Settings.Pixels_Per_Unit; }
	public static float ScreenX(float x) 
		{ return (x - center.x) * Settings.Pixels_Per_Unit + Settings.Screen_Width / 2; }
	public static float ScreenY(float y) 
		{ return (Settings.Screen_Height * 0.5f) - (y - center.y) * Settings.Pixels_Per_Unit;}
	
	// Convert from Screen to Simulation Coordinates
	public static float Sim(float val) 
		{ return val / Settings.Pixels_Per_Unit; }
	public static float SimX(float x) 
		{ return (x - Settings.Screen_Width / 2) / Settings.Pixels_Per_Unit + center.x; }
	public static float SimY(float y) 
		{ return -(y - Settings.Screen_Height / 2) / Settings.Pixels_Per_Unit + center.y; }
	
	@Override
	public int getID() { return id; }
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// Obtain User Input
		input = arg0.getInput();
		
		mousePanner = new MousePanner(); // Initialize Mouse Panner
		
		// Initialize the network
		network = new Network();
		
		// Initialize Center
		center = new Vector(30, 30);
		
		// Start Tracking Time
		time = System.currentTimeMillis();
		
		// Set Simulation Mode
		simulationMode = Mode.Random;
		
		// Testing
		Device one = new Device(5, 50);
		Device two = new Device(45, 75);
		Device three = new Device(70, 30);
		Device four = new Device(100, 45);
		Device five = new Device(120, 70);
		
		one.addConnection(two);
		two.addConnection(three);
//		three.addConnection(four);
		
		two.addConnection(four);
		four.addConnection(five);
		five.addConnection(one);
		
		network.addDevice(one);
		network.addDevice(two);
		network.addDevice(three);
		network.addDevice(four);
		network.addDevice(five);
		
		
		network.addPacket(new Packet(one, five));
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Render Simulation
		network.draw(g);
		
		g.setColor(Color.white);
		g.drawOval(Settings.Screen_Width / 2, Settings.Screen_Height / 2, 20, 20);
		
		if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
			g.drawOval(ScreenX(SimX(input.getMouseX())), ScreenY(SimY(input.getMouseY())), 20, 20);
		}
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// Handes Mouse Panning (Grab and Move)
		mousePanner.pan(input, center);
		
		if ( arg0.getAlwaysRender())
		
		if ( arg0.getInput().isKeyDown(Input.KEY_Z) ) {
			Settings.Pixels_Per_Unit -= 0.15f;
		}
		if ( arg0.getInput().isKeyDown(Input.KEY_X) ) {
			Settings.Pixels_Per_Unit += 0.15f;
		}
		// Update Simulation
		if ( arg0.getInput().isKeyDown(Input.KEY_P) ) {
			network.sendPacket();
		}
		
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