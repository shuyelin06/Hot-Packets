package core.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.Network;
import core.NetworkObject;
import core.geometry.Vector;
import engine.Settings;
import engine.Simulation;

public class Packet extends NetworkObject {	
	// Source Device and Destination Device (Final)
	private Device source;
	private Device destination;
	
	// Protocol used to send packet (TCP or UDP)
	private String protocol;
	
	// Network that packet belongs to
	private Network network;
	
	// Device Packet is Currently Traveling to
	private Device tempDestination;
	
	// Constructor
	public Packet(Device source, Device destination, String protocol, Network network) {
		super();
		
		// Set Variables
		this.source = source;
		this.destination = destination;
		this.protocol = protocol;
		this.network = network;
		
		// Set Position
		this.position = source.getPosition();
		
		// Temp
		this.tempDestination = destination;
	}
	
	
	// Packet Update
	public void update() {
		// Obtain Direction to Destination
		Vector direction = Vector.VectorDifference(position, 
					tempDestination.getPosition());
		// Normalize and Multiply by Speed
		Vector speed = direction.scalarMultiply(Settings.Packet_Speed / direction.magnitude());
		
		// Move to Destination
		position.x += speed.x;
		position.y += speed.y;
	
		// When Packet Reaches Destination
		boolean reached = false;
		if ( reached ) {
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
	
	/* Returns the source device of this packet
	 */
	public Device getSource() {
		return source;
	}
	
	public String getProtocol() {
		return protocol;
	}
}