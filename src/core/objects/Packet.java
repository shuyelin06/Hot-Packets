package core.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.NetworkObject;
import core.geometry.Vector;
import engine.Settings;
import engine.Simulation;

public class Packet extends NetworkObject {	
	// Source Device and Destination Device (Final)
	private Device source;
	private Device destination;
	
	// Device Packet is Currently Traveling to
	private Device tempDestination;
	
	// Constructor
	public Packet(Device source, Device destination) {
		super();
		
		// Set Variables
		this.source = source;
		this.destination = destination;
		
		// Set Position
		this.position = source.getPosition();
		
		// Temp
		source.protocol(this);
	}
	
	// Gets Destination
	public Device getDestination() { return destination; }
	
	// Sets the Next Device to Travel to
	public void nextDevice(Device d) {
		tempDestination = d;
	}
	
	// Packet Update
	public void update() {
		// Obtain Direction to Destination
		Vector direction = position.directionTo(tempDestination.getPosition());
		
		// Obtain Speed of Packet
		Vector speed = direction.scalarMultiply(Settings.Packet_Speed);
		
		// Move to Destination
		position.x += speed.x;
		position.y += speed.y;
	
		// When Packet Reaches Destination
		if ( position.distance(tempDestination.getPosition()) < 2.5f ) {
			tempDestination.protocol(this);
		}
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(Simulation.ScreenX(position.x - width / 2), 
				Simulation.ScreenY(position.y + height / 2), 
				Simulation.Screen(width), Simulation.Screen(height));
	}
}