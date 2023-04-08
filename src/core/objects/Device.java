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
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		
	}
	
}