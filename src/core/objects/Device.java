package core.objects;

import java.util.ArrayList;
import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Network;
import core.NetworkObject;
import core.geometry.Vector;
import engine.Rule;
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
	private ArrayList<Rule> rules;
	
	// Device Information
	private String name;
	private int[] ip = new int[5];
	
	// Device image (same for every device object)
	private static Image device_image;
	
	
	// Get Connections
	public ArrayList<Device> getConnections() { return connections; }
	
	// Device Color Representation
	private Color deviceColor;
	
	// Constructor
	public Device(float x, float y) {
		super();
		
		// Add to Network
		Network.getInstance().addDevice(this);
		
		// Set Postion
		position.x = x;
		position.y = y;
		
		// Initialize Variables
		rules = new ArrayList<>();
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
	public void insertRule(Rule.RuleType rule, int[] sourceIP, int netmask,
			Packet.Protocol protocol) {
		Rule newRule = new Rule(rule, sourceIP, netmask, protocol);
		if (!hasRule(newRule))
			rules.add(0, newRule);
	}
	
	/* Appends a rule to the end of the iptable of this device
	 */
	public void appendRule(Rule.RuleType rule, int[] sourceIP, int netmask, 
			Packet.Protocol protocol) {
		Rule newRule = new Rule(rule, sourceIP, netmask, protocol);
		if (!hasRule(newRule))
			rules.add(newRule);
	}
	
	// Deletes iptable rule
	public void deleteRule(Rule rule) {
		rules.remove(rule);
	}
	
	/* Checks if the iptable rule exists. Returns true if it exists, returns
	 * false if it does not.
	 */
	public boolean hasRule(Rule rule) {
		return rules.contains(rule);
	}
	
	// Protocol Method
	// Performs a protocol on a packet that has reached it
	/* Checks if the packet matches any rules and drops or accepts it based on
	 * the rule matched
	 */
	public void protocol(Packet packet) {
	
	    for (Rule curRule : rules) {
	        // if there is a matching rule
	        if (hasMatchingIP(packet, curRule) && 
	            packet.getProtocol() == (curRule.getProtocol())) {
	
	          // TCP protocol
	          if (packet.getProtocol() == Packet.Protocol.TCP) {
	
	            // DROP (silently delete)
	            if (curRule.getRule() == Rule.RuleType.DROP) {
	              packet.setStatus(Status.Dead);
	
	            // REJECT (notify of deletion)
	            } else if (curRule.getRule() == Rule.RuleType.REJECT) {
	            	packet.setStatus(Status.Dead);
	              // notify source device that it was rejected
	
	            // ACCEPT, default
	            } else {
	              // notify source device that it was accepted
	            }
	
	          // UDP protocol
	          } else if (packet.getProtocol() == Packet.Protocol.UDP) {
	
	            // DROP (silently delete)
	            if (curRule.getRule() == Rule.RuleType.DROP) {
	            	packet.setStatus(Status.Dead);
	
	            // REJECT (silently delete)
	            } else if (curRule.getRule() == Rule.RuleType.REJECT) {
	            	packet.setStatus(Status.Dead);
	
	            } // No ACCEPT option, because accept does nothing
	          }
	
	        }
	        break;
	      }
	    

		Device next;
		
		if ( packet.getDestination() == this ) {
			next = null;
		} else {
			next = routePacket(packet.getDestination(), null);
		}
		
		packet.nextDevice(next);
	}
	
	// Checks if the ip of the packet matches the ip of the rule
	private boolean hasMatchingIP(Packet packet, Rule rule) {
		boolean result = false;
		
		int[] sourceIP = packet.getSourceIP();
		int[] ruleIP = rule.getSourceIP();
		int netmask = rule.getNetmask();

		if (netmask == 0) {
			result = true;
		
		} else if (netmask == 8) {
			if (sourceIP[0] == ruleIP[0])
				result = true;
				
		} else if (netmask == 16) {
			if (sourceIP[0] == ruleIP[0] && sourceIP[1] == ruleIP[1])
				result = true;
			
		} else if (netmask == 24) {
			if (sourceIP[0] == ruleIP[0] && sourceIP[1] == ruleIP[1] &&
					sourceIP[2] == ruleIP[2])
				result = true;
			
		} else if (netmask == 32) {
			if (sourceIP[0] == ruleIP[0] && sourceIP[1] == ruleIP[1] &&
					sourceIP[2] == ruleIP[2] && sourceIP[3] == ruleIP[3])
				result = true;
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