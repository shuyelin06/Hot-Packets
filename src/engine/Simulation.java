package engine;

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
		{ return (x - center.x) * Settings.Pixels_Per_Unit; }
	public static float ScreenY(float y) 
		{ return (Settings.Screen_Height) - (y - center.y) * Settings.Pixels_Per_Unit;}
	
	@Override
	public int getID() { return id; }
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// Initialize the network
		network = new Network();
		
		// Initialize Center
		center = new Vector(0,0);
		
		// Start Tracking Time
		time = System.currentTimeMillis();
		
		// Testing
		Device one = new Device(5, 50);
		Device two = new Device(45, 75);
		
		network.addDevice(one);
		network.addDevice(new Device(10, 5));
		network.addDevice(new Device(30, 5));
		network.addDevice(two);
		
		network.addPacket(new Packet(one, two));
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Render Simulation
		network.draw(g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// Zoom in and out
		if ( arg0.getInput().isKeyDown(Input.KEY_Z) ) {
			Settings.Pixels_Per_Unit -= 0.15f;
		}
		if ( arg0.getInput().isKeyDown(Input.KEY_X) ) {
			Settings.Pixels_Per_Unit += 0.15f;
		}
		// Pan left when A is held down
		if ( arg0.getInput().isKeyDown(Input.KEY_A) ) {
			center.x -= 0.5f;
		}
		// Pan right when D is held down
		if ( arg0.getInput().isKeyDown(Input.KEY_D) ) {
			center.x += 0.5f;
		}
		// Pan down when S is held down
		if ( arg0.getInput().isKeyDown(Input.KEY_S) ) {
			center.y -= 0.5f;
		}
		// Pan up when W is held down
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