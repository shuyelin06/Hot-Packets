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
	
	// Adds an Outgoing Connection
	public void addConnection(Device d) {
		connections.add(d);
	}
	
	// Protocol Method
	// Performs a protocol on a packet that has reached it
	public void protocol(Packet packet) {
		Device next = nextDevice(packet.getDestination());
		packet.nextDevice(next);
	}
	
	//
	public Device nextDevice(Device destination) {
	ArrayList<Device> potential = new ArrayList<Device>();
	ArrayList<Device> traveled = new ArrayList<Device>();
	traveled.add(this);
		for(int i = 0; i < connections.size(); i++) {
			if(nextDeviceAux(connections.get(i), destination, traveled)) {
				potential.add(connections.get(i));
			}
		}
		return potential.get((int) (0));
	}
	
	private boolean nextDeviceAux(Device curr, Device destination, ArrayList<Device> traveled) {
		boolean notTraveled = true;		
		for(int i = 0; i < curr.connections.size(); i++) {
			for(int j = 0; j < traveled.size(); j++) {
				if (traveled.get(j).equals(curr.connections.get(i))) {
					notTraveled = false;
				}
			}
			if(curr.connections.get(i).equals(destination) && notTraveled) {
				return true;
			} else if (!notTraveled){
				continue;
			} else {
				traveled.add(this);
				nextDeviceAux(curr.connections.get(i), destination, traveled);
			}
		}
		
		return true;
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
		
		// Draw Connection
		g.setColor(Color.green);
	}
	
}