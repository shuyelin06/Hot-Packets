package engine;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import core.Network;
import core.geometry.Vector;
import core.objects.Device;
import core.objects.Packet;

public class Simulation extends BasicGameState {
	private int id;
	
	// Center of the Simulation
	static private Vector center;
		
	// Track Time (Milliseconds)
	private long time;
	
	// Network the Simulation Uses
	private Network network;
	
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
	
	@Override
	public int getID() { return id; }
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// Initialize the network
		network = new Network();
		
		// Initialize Center
		center = new Vector(30, 30);
		
		// Start Tracking Time
		time = System.currentTimeMillis();
		
		// Testing
		Device one = new Device(5, 50);
		Device two = new Device(45, 75);
		Device three = new Device(70, 30);
		Device four = new Device(100, 45);
		
		one.addConnection(two);
		two.addConnection(three);
		three.addConnection(four);
		
		two.addConnection(four);
		
		network.addDevice(one);
		network.addDevice(two);
		network.addDevice(three);
		network.addDevice(four);
		
		
//		network.addPacket(new Packet(one, four));
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Render Simulation
		network.draw(g);
		
		g.setColor(Color.white);
		g.drawOval(Settings.Screen_Width / 2, Settings.Screen_Height / 2, 20, 20);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		if ( arg0.getInput().isKeyDown(Input.KEY_Z) ) {
			Settings.Pixels_Per_Unit -= 0.15f;
		}
		if ( arg0.getInput().isKeyDown(Input.KEY_X) ) {
			Settings.Pixels_Per_Unit += 0.15f;
		}
		// Update Simulation
		if ( arg0.getInput().isKeyDown(Input.KEY_A) ) {
			center.x -= 0.5f;
		}
		if ( arg0.getInput().isKeyDown(Input.KEY_D) ) {
			center.x += 0.5f;
		}
		if ( arg0.getInput().isKeyDown(Input.KEY_S) ) {
			center.y -= 0.5f;
		}
		if ( arg0.getInput().isKeyDown(Input.KEY_W) ) {
			center.y += 0.5f;
		}
		
		float timeDifference = (System.currentTimeMillis() - time) / 1000f;
		
		while ( timeDifference > 1 / Settings.Ticks_Per_Second ) {
			network.update();
			
			timeDifference -= 1 / Settings.Ticks_Per_Second;
		}
		
		time = System.currentTimeMillis();
	}
}