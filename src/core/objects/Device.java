package core.objects;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.NetworkObject;
import core.geometry.Vector;
import engine.Simulation;

/*
 * Device Class:
 * 
 * Basic class representing a node 
 * in the graph.
 */
public class Device extends NetworkObject {
	// List of Outward Connections (Can Send Data to)
	private ArrayList<Device> connections;
	
	// Device Information
	private String name;
	private String ip;
	
	// Get Connections
	public ArrayList<Device> getConnections() { return connections; }
	
	// Constructor
	public Device(float x, float y) {
		super();
		
		// Set Postion
		position.x = x;
		position.y = y;
		
		// Initialize Variables
		connections = new ArrayList<>();
		
		name = "NULL";
		ip = "NULL";
	}
	
	// Protocol Method
	// Performs a protocol on a packet that has reached it
	public void protocol(Packet packet) {
		
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		// Draw Device (Circle)
		g.setColor(Color.blue);
		g.drawOval(
			Simulation.ScreenX(position.x - width / 2), 
			Simulation.ScreenY(position.y + height / 2), 
			Simulation.Screen(width), Simulation.Screen(height));
	}
	
}