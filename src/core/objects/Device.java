package core.objects;

import java.util.ArrayList;
import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Network;
import core.NetworkObject;
import core.NetworkObject.Status;
import core.geometry.Vector;
import core.protocols.FilterRule;
import core.protocols.NatRule;
import core.protocols.Rule;
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
	
	// List of iptable filter rules
	private ArrayList<FilterRule> FilterRules;
	
	private ArrayList<NatRule> NatRules;
	
	// Device Information
	private String name;
	private int[] ip = new int[5];
	
	// Device image (same for every device object)
	private static Image device_image;
	
	
	// Get Connections
	public ArrayList<Device> getConnections() { return connections; }
	
	// Device Color Representation
	private Color deviceColor;
	
	/* Device Statistics */
	private int packetsSent; // Packets Sent Through Device
	private int packetsReceived; // Packets Received
	
	// Constructor
	public Device(float x, float y) {
		super();
		
		// Add to Network
		Network.getInstance().addDevice(this);
		
		// Statistics
		packetsSent = 0;
		
		// Set Postion
		position.x = x;
		position.y = y;
		
		// Initialize Variables
		FilterRules = new ArrayList<>();
		NatRules = new ArrayList<>();
		connections = new ArrayList<>();
		
		name = "NULL";
		
		width = 7;
		height = 7;
		
		deviceColor = new Color((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255));
	}
	
	public Color getColor() { return deviceColor; }
	
	// Returns a String Representing its IP
	public String ipString() {
		return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
	}
	
	// Gets an Array of Strings Describing the Device
	public void getInfo(ArrayList<String> info) {
		info.add("Device");
		info.add("==========");
		info.add("IP: " + ipString());
		info.add("Packets Sent: " + packetsSent);
		info.add("Packets Received: " + packetsReceived);
		
		info.add("---");
		for ( Rule r : FilterRules ) {
			r.getInfo(info);
		}
	}
		
	// Adds an Outgoing Connection
	public void addConnection(Device d) {
		connections.add(d);
	}
	
	// Sets the IP
	public void setIP(int a1, int a2, int a3, int a4) {
		ip[0] = a1;
		ip[1] = a2;
		ip[2] = a3;
		ip[3] = a4;
	}
	
	public int[] getIP() {
		return ip;
	}
  
	/* inserts a rule to the top of the iptable of this device
	 */
	public void insertRule(FilterRule.RuleType rule, int[] sourceIP, 
			int sNetmask, int[] destIP, int dNetmask, Packet.Protocol protocol) {
		FilterRule newRule = new FilterRule(rule, sourceIP, sNetmask, destIP,
				dNetmask, protocol);
		if (!hasRule(newRule))
			FilterRules.add(0, newRule);
	}
	
	// Deletes iptable rule
	public void deleteRule(FilterRule rule) {
		FilterRules.remove(rule);
	}
	
	/* Checks if the iptable rule exists. Returns true if it exists, returns
	 * false if it does not.
	 */
	public boolean hasRule(Rule rule) {
		return FilterRules.contains(rule);
	}
	
	// Protocol Method
	// Performs a protocol on a packet that has reached it
	/* Checks if the packet matches any rules and drops or accepts it based on
	 * the rule matched
	 */
	public void protocol(Packet packet) {
		/* Increment Device Statistics */
		if ( packet.getSource() == this ) { this.packetsSent++; } 
		if ( packet.getDestination() == this ) { this.packetsReceived++; } 
		
		if (packet.getStatus() == Packet.Status.Lost) {
			new Packet(packet.getSource(), packet.getDestination(), 
					packet.getProtocol());
			packet.setStatus(Status.Dead);
		}
	
		// applies filter rules if applicable
	    for (FilterRule curRule : FilterRules) {
	        // if there is a matching rule
	        if (hasMatchingIP(packet, curRule) &&
	            packet.getProtocol() == (curRule.getProtocol())) {
	
	          // TCP protocol
	          if (packet.getProtocol() == Packet.Protocol.TCP) {
	
	            // DROP (silently delete)
	            if (curRule.getRule() == FilterRule.RuleType.DROP) {
	              packet.setStatus(Status.Dead);
	
	            // REJECT (notify of deletion)
	            } else if (curRule.getRule() == FilterRule.RuleType.REJECT) {
	            	packet.setStatus(Status.Dead);
	              // notify source device that it was rejected
	
	            // ACCEPT, default
	            } else {
	              // notify source device that it was accepted
	            }
	
	          // UDP protocol
	          } else if (packet.getProtocol() == Packet.Protocol.UDP) {
	
	            // DROP (silently delete)
	            if (curRule.getRule() == FilterRule.RuleType.DROP) {
	            	packet.setStatus(Status.Dead);
	
	            // REJECT (silently delete)
	            } else if (curRule.getRule() == FilterRule.RuleType.REJECT) {
	            	packet.setStatus(Status.Dead);
	
	            } // No ACCEPT option, because accept does nothing
	          }
	
	        }
	        break;
	      }
	    
	    // applies nat rules if applicable
	    applyNatRule(packet);
	    

		Device next;
		
		if ( packet.getDestination() == this ) {
			next = null;
		} else {
			next = routePacket(packet.getDestination(), null);
		}
		
		packet.nextDevice(next);
	}
	
	public void applyNatRule(Packet packet) {
		for (NatRule rule : NatRules) {
			int[] oldIP = rule.getOldIP();
			boolean match = true;
			
			// SNAT
			if (rule.getNatType() == NatRule.NatType.SNAT) {
				int[] packetIP = packet.getSourceIP();
				
				// Checks if IPs match
				for (int i = 0; i < oldIP.length; i++) {
					if (oldIP[i] != packetIP[i])
						match = false;
				}
				// IPs match, need to perform snat
				if (match) {
					packet.setSourceIP(rule.getNewIP());
					return;
				}
				
			// DNAT
			} else if (rule.getNatType() == NatRule.NatType.DNAT) {
				int[] packetIP = packet.getDestIP();
				
				// Checks if IPs match
				for (int i = 0; i < oldIP.length; i++) {
					if (oldIP[i] != packetIP[i])
						match = false;
				}
				// IPs match, need to perform dnat
				if (match) {
					packet.setDestIP(rule.getNewIP());
					return;
				}
			}
		}
	}
	
	// Checks if the ip of the packet matches the ip of the rule, for both
	// source and destination IPs of the packet
	private boolean hasMatchingIP(Packet packet, FilterRule rule) {
		boolean result = false;
		
		int[] sourceIP = packet.getSourceIP();
		int[] destIP = packet.getDestIP();
		int[] ruleSourceIP = rule.getSourceIP();
		int[] ruleDestIP = rule.getDestIP();
		int sNetmask = rule.getSNetmask();
		int dNetmask = rule.getDNetmask();
		
		
		if (ipIsInRange(ruleSourceIP, sNetmask, sourceIP) && 
				ipIsInRange(ruleDestIP, dNetmask, destIP))
			result = true;
			
		return result;
	}
	
	// Checks if ip2 is in the rage of ip1/netmask
	private boolean ipIsInRange(int[] ip1, int netmask, int[] ip2) {
		boolean result = true;
		for (int i = 0; i < netmask / 8; i++) {
			if (ip2[i] != ip1[i])
				result = false;
		}
		return result;
	}
	
	// Returns the next device to send the packet to 
	public Device routePacket(Device destination, HashSet<Device> visited) {
		// Base Case: Check if We've Found Destination
		if ( this == destination ) {
			return this;
		}
		
		if ( visited == null ) {
			visited = new HashSet<Device>();
		}
		
		for ( Device d : connections ) {
			if ( visited != null && visited.contains(d) ) {
				continue;
			} else {
				visited.add(d);
				if ( d.routePacket(destination, visited) != null ) {
					return d;
				} 
			}
			
		}
		
		return null;	
	}

	// Returns a Random Node this Node is Connected to
	// Depth controls how long these connections can be
	public Device randomConnection(int depth) {
		if ( depth == 0 || connections.size() == 0 ) {
			return this;
		}
		
		int random = (int) (Math.random() * connections.size());
		return connections.get(random).randomConnection(depth - 1);
   }
		
	
	public void dropPacket(Packet packet) {
		
	}
	
	public void acceptPacket(Packet packet) {

	}
	
	// Instantiate device image
	public static Image get_image() {
		if ( device_image == null ) {
			try {
				device_image = new Image("res/Device.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return device_image;
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {		
		// Instantiate device image
		Image scaledDeviceImg = get_image().getScaledCopy(
				(int)Simulation.Screen(width), (int)Simulation.Screen(height));
		
		// Draw device (device drawing)
		g.drawImage(scaledDeviceImg, 
				Simulation.ScreenX(position.x - width / 2), 
				Simulation.ScreenY(position.y + height / 2));
		
		// Draw Connection
		for ( Device dest : connections ) {
			drawEdge(dest, g);
		}
	}
	
	// Draw Edge
	private void drawEdge(Device d, Graphics g) {
		g.setColor(new Color(211, 211, 211, 200));
		g.setLineWidth(4);
		
		// Get direction to destination
		Vector direction = position.directionTo(d.position);
		float distance = position.distance(d.position);
		
		// Arrow End
		float arrowEndX = position.x + direction.x * distance * 0.925f;
		float arrowEndY = position.y + direction.y * distance * 0.925f;
		
		// Draw Arrow
		g.drawLine(
				Simulation.ScreenX(position.x), Simulation.ScreenY(position.y), 
				Simulation.ScreenX(arrowEndX), 
				Simulation.ScreenY(arrowEndY));
		
		// Draw Arrow Points
		Vector point1 = direction.rotate((float) Math.toRadians(150)).scalarMultiply(1.5f);
		Vector point2 = direction.rotate((float) Math.toRadians(-150)).scalarMultiply(1.5f);
		
		g.drawLine(Simulation.ScreenX(arrowEndX), Simulation.ScreenY(arrowEndY), 
				Simulation.ScreenX(arrowEndX + point1.x), 
				Simulation.ScreenY(arrowEndY + point1.y));
		g.drawLine(Simulation.ScreenX(arrowEndX), Simulation.ScreenY(arrowEndY), 
				Simulation.ScreenX(arrowEndX + point2.x), 
				Simulation.ScreenY(arrowEndY + point2.y));
		
		g.resetLineWidth();
	}
}