package engine;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import core.Network;
import core.NetworkObject;
import core.geometry.Vector;
import core.objects.Device;
import core.objects.Packet;
import graphics.Box;
import graphics.boxes.InfoBox;
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
	
	// Game Entity Selected
	private NetworkObject selectedEntity;
	
	// Graphics
	private ArrayList<Box> boxes;
	private InfoBox infoBox; // Information Box 
	private Box simulationBox; // Simulation (Settings) Box
	private Box commandBox; // Command Box
	
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
		// Initialize Boxes ArrayList (Graphics)
		boxes = new ArrayList<>();
		
		infoBox = new InfoBox(); // Add InfoBox
		infoBox
			.setX(Settings.Screen_Width - 100)
			.setY(475)
			.setWidth(150)
			.setHeight(200);
		
		simulationBox = new Box();
		simulationBox
			.setX(Settings.Screen_Width - 100)
			.setY(200)
			.setWidth(150)
			.setHeight(300);
		
		commandBox = new Box() ;
		commandBox
			.setX(Settings.Screen_Width / 2)
			.setY(Settings.Screen_Height * 0.9f)
			.setWidth(Settings.Screen_Width * 0.95f)
			.setHeight(Settings.Screen_Height * 0.15f);
		
		boxes.add(infoBox);
		boxes.add(simulationBox);
		boxes.add(commandBox);
		
		// Obtain User Input
		input = arg0.getInput();
		
		mousePanner = new MousePanner(); // Initialize Mouse Panner
		selectedEntity = null; // Set Selected Object to null
		
		
		// Obtain Network
		network = Network.getInstance();
		
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
		
		two.insertRule("DROP", one, "TCP");
		
		one.addConnection(two);
		two.addConnection(three);
//		three.addConnection(four);
		
		two.addConnection(four);
		four.addConnection(five);
		five.addConnection(one);
		
		new Packet(one, two, "TCP", network);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Render Simulation
		network.draw(g);
		
		g.setColor(Color.white);
		g.drawOval(Settings.Screen_Width / 2, Settings.Screen_Height / 2, 20, 20);
		
		// Draw Boxes
		for ( Box b : boxes ) {
			b.draw(g);
		}
		
		if ( selectedEntity != null ) {
			g.setColor(Color.yellow);
			g.drawOval(
					ScreenX(selectedEntity.getX()), ScreenY(selectedEntity.getY()), 
					Screen(10), Screen(10));
		}
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		if ( input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ) {
			infoBox.setSelected(network.searchForObject(
					Simulation.SimX(input.getMouseX()), Simulation.SimY(input.getMouseY())));
		}
		
		// Handes Mouse Panning (Grab and Move)
		mousePanner.pan(input, center);
		
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