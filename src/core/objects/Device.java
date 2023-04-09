package core.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Network;
import core.NetworkObject;
import core.NetworkObject.Status;
import core.geometry.Vector;
import core.objects.Packet.Protocol;
import core.protocols.FilterRule;
import core.protocols.NatRule;
import core.protocols.Rule;
import engine.Settings;
import engine.Simulation;
import graphics.MessageBoard;

/*
 * Device Class:
 * 
 * Basic class representing a node 
 * in the graph.
 */
public class Device extends NetworkObject {
	
	// Device Image Singleton
	private static Image device_image;
	
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
		
	// List of Outward Connections (Can Send Data to)
	private ArrayList<Device> connections;
	
	// List of Iptable Filter and NAT Rules
	private ArrayList<FilterRule> FilterRules;
	private ArrayList<NatRule> NatRules;
	
	// Device Information
	private int[] ip = new int[5];
	
	// Device Configurations
	private float traffic; // Traffic - Controls how many packets a device can take at a time.
	private boolean ping; // Tracks if a Device will Constantly Ping
	private Color deviceColor; // Device Color Representation
	
	// Device Statistics
	private MessageBoard messagesReceived; 	// Messages (Recently) Received
	
	private int packetsSent; // Packets Sent Through Device
	private int packetsReceived; // Packets Received
	
	// Constructor
	public Device(float x, float y) {
		super();
		
		// Add to Network
		Network.getInstance().addDevice(this);
		
		// Statistics
		messagesReceived = new MessageBoard(Color.green);
		
		packetsReceived = 0;
		packetsSent = 0;
		
		// Set Postion
		position.x = x;
		position.y = y;
		
		// Initialize Variables
		ping = false;
		
		FilterRules = new ArrayList<>();
		NatRules = new ArrayList<>();
		connections = new ArrayList<>();
		
		traffic = 0f;
		
		width = 7;
		height = 7;
		
		deviceColor = new Color((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255));
	}
	
	/* Setters */ 
	// Set the Device IP
	public Device setIP(int[] ip) { this.ip = ip; return this; } 
	// Set if the Device Should Ping
	public Device setPing(boolean ping) { this.ping = ping; return this; }
	
	// Sets the IP
	public void setIP(int a1, int a2, int a3, int a4) {
		ip[0] = a1; ip[1] = a2; ip[2] = a3; ip[3] = a4;
	}
		
	/* Getters */
	// Get Connections
	public ArrayList<Device> getConnections() { return connections; }
	// Get Device Color (Colors Packets)
	public Color getColor() { return deviceColor; }
		
	// Returns True if the device has been set to ping 
	public boolean getPing() { return ping; }
	// Returns True if the device is congested (not accepting packets)
	public boolean congested() { return traffic < 1f; }
	
	// Sets Device IP
	public int[] getIP() { return ip; }
	// Returns a String Representing device  IP
	public String ipString() { return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3]; }
	
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
		for ( Rule r : NatRules ) {
			r.getInfo(info);
		}
	}
	
	
	/* Device Configurations */
	// Adds an Outgoing Connection
	public void addConnection(Device d) {
		if ( !connections.contains(d) ) {
			connections.add(d);
		}
	}
	
	
	// Adds NAT Rule
	public void newNATRule(NatRule rule) { NatRules.add(0,rule); }
	// Remove NAT Rule
	public void removeNATRule(NatRule rule) { NatRules.remove(rule); }
	
	// Adds Filter Rule
	public void newFilterRule(FilterRule rule) { FilterRules.add(0, rule); }
	// Deletes Filter Rule
	public void deleteRule(FilterRule rule) { FilterRules.remove(rule); }
	
	/* Device Updating */
	// Update Device
	public void update() {
		// If Ping Setting is True, Ping Other Devices
		if ( ping ) {
			if ( Math.random() < 0.075f) 
				new Packet(this, randomConnection(4), Math.random() < 0.5 ? Protocol.TCP : Protocol.UDP);
		}
		
		// Traffic Settings
		traffic += Settings.Max_Traffic;
		if ( Settings.Max_Traffic > 1f) {
			if ( traffic > Settings.Max_Traffic )
				traffic = Settings.Max_Traffic;
		} else {
			if ( traffic > 1f ) 
				traffic = 1f;
		}
		
		messagesReceived.update();
	}
	
	/* Device Protocol */
	// Performs a protocol on packets reaching the device, based on rules matched
	public void protocol(Packet packet) {
		// Decrease Traffic the Device Can Take by 1
		traffic -= 1f;
		
		/* Increment Device Statistics */
		if ( packet.getSource() == this ) { 
			this.packetsSent++; 
		} 
		if ( packet.getDestination() == this ) {
			messagesReceived.addMessage(packet.getMessage());
			this.packetsReceived++; 
		} 
		
		// Resent Lost Packets
		if (packet.getStatus() == Packet.Status.Lost) {
			new Packet(packet.getSource(), packet.getDestination(), 
					packet.getProtocol());
			packet.setStatus(Status.Dead);
		}
	
		// Apply Filter Rules (if applicable)
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
	    
	    // Apply NAT Rules (if applicable)
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
	    
		// Route packet to next device
		Device next;
		
		if ( packet.getDestination() == this ) {
			next = null;
		} else {
			next = routePacket(packet.getDestination(), null);
		}
		
		packet.nextDevice(next);
	}
	
	// Checks if the ip of the packet matches the ip of the rule, for both
	// source and destination IPs of the packet
	private boolean hasMatchingIP(Packet packet, FilterRule rule) {
		boolean result = false;
		
		int[] sourceIP = packet.getSourceIP();
		int[] destIP = packet.getDestIP();
		int[] ruleSourceIP = rule.getSourceIP();
		int[] ruleDestIP = rule.getDestIP();
		
		
		if (ipIsInRange(ruleSourceIP, sourceIP) && 
				ipIsInRange(ruleDestIP, destIP))
			result = true;
			
		return result;
	}
	
	// Checks if ip2 is in the rage of ip1/netmask
	private boolean ipIsInRange(int[] ip1, int[] ip2) {
		boolean result = true;
		for (int i = 0; i < ip1.length; i++) {
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
		
		// Randomize the pathfinding
		Collections.shuffle(connections);
		
		for ( Device d : connections ) {
			if (visited.contains(d) ) {
				continue;
			} else {
				visited.add(d);
				if ( d.routePacket(destination, visited) != null ) {
					return d;
				} 
				visited.remove(d);
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
		
		// Draw Received Messages
		messagesReceived.render(g, 
			Simulation.ScreenX(position.x + width / 2), 
			Simulation.ScreenY(position.y + height / 2));
	}
	
	// Draw Edge
	private void drawEdge(Device d, Graphics g) {
		g.setColor(new Color(211, 211, 211, 200));
		g.setLineWidth(Settings.Pixels_Per_Unit / 2.75f);
		
		// Get direction to destination
		Vector direction = position.directionTo(d.position);
		float distance = position.distance(d.position);
		
		// Arrow End
		float arrowEndX = position.x + direction.x * distance * 0.825f;
		float arrowEndY = position.y + direction.y * distance * 0.825f;
		
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