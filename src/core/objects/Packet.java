package core.objects;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.Network;
import core.NetworkObject;
import core.NetworkObject.Status;
import core.geometry.Vector;
import engine.Settings;
import engine.Simulation;

public class Packet extends NetworkObject {	
	
	public enum Protocol { TCP, UDP }
	
	// Source Device and Destination Device (Final)
	private Device source;
	private Device destination;
	
	private int[] sourceIP;
	
	// Protocol used to send packet (TCP or UDP)
	private Protocol protocol;
	
	// Device Packet is Currently Traveling to
	private Device tempDestination;
	
	// Constructor
	public Packet(Device source, Device destination, Protocol protocol, Network network) {
		super();
		
		// Add to Network
		Network.getInstance().addPacket(this);
		
		// Set Variables
		this.source = source;
		this.destination = destination;
		this.protocol = protocol;
		
		sourceIP = source.getIP();
		
		// Set Position
		this.position = source.getPosition();
		
		// Set Width and Height
		this.width = 1f;
		this.height = 1f;
		
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
		
		// 15% chance of packet getting lost
        if (Math.random() < 0.001) {
        	setStatus(Status.Dead);
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
		g.setColor(source.getColor());
		g.fillRect(Simulation.ScreenX(position.x - width / 4), 
				Simulation.ScreenY(position.y + height / 4), 
				Simulation.Screen(width / 2), Simulation.Screen(height / 2));
	}
	
	// Sets Packet Status
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/* Returns the source device of this packet
	 */
	public Device getSource() {
		return source;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}
	

	// Gets an Array of Strings Describing the Device
	public void getInfo(ArrayList<String> info) {
		info.add("Packet");
		info.add("==========");
	}

	public int[] getSourceIP() {
		return sourceIP;
	}
	
}