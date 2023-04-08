package core.objects;

import org.newdawn.slick.Graphics;

import core.NetworkObject;
import core.geometry.Vector;

public class Packet extends NetworkObject {	
	// Source Device and Destination Device (Final)
	private Device source;
	private Device destination;
	
	// Device Packet is Currently Traveling to
	private Device tempDestination;
	
	// Packet Update
	public void update() {
		// Position of Destination Device
		Vector dest = tempDestination.getPosition();
		
		// Packet Moves to Destination
		
		
		
		
		// When Packet Reaches Destination
		boolean reached = false;
		if ( reached ) {
			tempDestination.protocol(this);
		}
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		
	}
}