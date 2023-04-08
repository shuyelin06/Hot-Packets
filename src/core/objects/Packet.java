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
		
		// Set Width and Height
		this.width = 0.5f;
		this.height = 0.5f;
		
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
		if ( tempDestination == null ) {
			status = Status.Dead;
			return;
		}
		
		// Obtain Direction to Destination
		Vector direction = position.directionTo(tempDestination.getPosition());
		
		// Obtain Speed of Packet
		Vector speed = direction.scalarMultiply(Settings.Packet_Speed);
		
		// Move to Destination
		position.x += speed.x;
		position.y += speed.y;
	
		// When Packet Reaches Destination
		if ( position.distance(tempDestination.getPosition()) < 1f ) {
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