package core.objects;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import core.NetworkObject;
import core.geometry.Vector;

/*
 * Device Class:
 * 
 * Basic class representing a node 
 * in the graph.
 */
public class Device extends NetworkObject {
	// Unique Identifier for a Device
	private int identifier;
	
	// List of Outward Connections (Can Send Data to)
	private ArrayList<Device> connections;
	
	// Device Information
	private String name;
	private String ip;
	
	// Protocol Method
	// Performs a protocol on a packet that has reached it
	public void protocol(Packet packet) {
		
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
		return potential.get((int) ((Math.random() * (2 - 0)) + 0));
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
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		
	}
	
}{
	
}