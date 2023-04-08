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
	private int[] destIP;
	
	// Protocol used to send packet (TCP or UDP)
	private Protocol protocol;
	
	// Device Packet is Currently Traveling to
	private Device tempDestination;
	
	// Message the Packet is Carrying 
	private String message;
	
	// Constructor
	public Packet(Device source, Device destination, Protocol protocol) {
		super();
		
		// Add to Network
		Network.getInstance().addPacket(this);
		
		// Set Variables
		this.source = source;
		this.destination = destination;
		this.protocol = protocol;
		
		sourceIP = source.getIP(); 		// Source IP
		destIP = destination.getIP(); 	// Destination IP
		
		message = "[Empty Message]"; 	// Empty Message
		
		// Set Position
		this.position = source.getPosition();
		
		// Set Width and Height
		this.width = 1.5f;
		this.height = 1.5f;
		
		source.protocol(this);
	}
	
	// Gets Destination
	public Device getDestination() { return destination; }
	
	
	// Get Message
	public String getMessage() { return message; }
	
	// Sets the Next Device to Travel to
	public void nextDevice(Device d) {
		tempDestination = d;
	}
	
	// Packet Update
	public void update() {
		
		if (status != Status.Dead) {
		
			if ( tempDestination == null ) {
				status = Status.Dead;
				return;
			}
			
			// small chance of packet getting lost, change to 0.001 for final
	        if (Math.random() < 0.001) {
	        	if (protocol == Protocol.TCP) {
	        		setStatus(Status.Lost);
	        		
	        	} else if (protocol == Protocol.UDP) {
	        		setStatus(Status.Dead);
	
	        	}
	        }
			
        	// Obtain Direction to Destination
			Vector direction = position.directionTo(tempDestination.getPosition());
			
			// Obtain Speed of Packet
			Vector speed = direction.scalarMultiply(Settings.Packet_Speed);
			if ( tempDestination.congested() ) {
				speed = speed.scalarMultiply(0.2f);
			}
			
			// Move to Destination
			position.x += speed.x;
			position.y += speed.y;
		
			// When Packet Reaches Destination
			if (position.distance(tempDestination.getPosition()) < 1f ) {
				tempDestination.protocol(this);
			}
			
		}
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		if ( status == Status.Alive ) {
			g.setColor(source.getColor());
			g.fillRect(Simulation.ScreenX(position.x - width / 4), 
					Simulation.ScreenY(position.y + height / 4), 
					Simulation.Screen(width / 2), Simulation.Screen(height / 2));
		}
		
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
		
		if ( status == Status.Alive ) {
			info.add("Source:" + source.ipString());
			
			info.add("Destination: " + destination.ipString());
			
			info.add("---");
			
			// Packet Protocol
			String stringProtocol;
			if ( protocol == Protocol.TCP ) {
				stringProtocol = "TCP";
			} else {
				stringProtocol = "UDP";
			}
			info.add("Protocol: " + stringProtocol);	
			
			// Packet Message
			info.add("Message: " + message); 
			
		} else if ( status == Status.Lost ) {
			info.add("Packet LOST");
			
			if ( protocol == Protocol.TCP ) {
				info.add("Resending...");
			}
		}
		
		
		
	}

	public int[] getSourceIP() {
		return sourceIP;
	}
	
	public int[] getDestIP() {
		return destIP;
	}
	
	public void setSourceIP(int[] ip) {
		sourceIP = ip;
	}
	
	public void setDestIP(int[] ip) {
		destIP = ip;
	}
	
}