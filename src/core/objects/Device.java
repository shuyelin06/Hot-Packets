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
	private String ip;
	
	// Device image (same for every device object)
	private static Image device_image;
	private static Image scaledDeviceImg;
	
	
	// Get Connections
	public ArrayList<Device> getConnections() { return connections; }
	
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
		ip = "NULL";
		
		width = 7;
		height = 7;
	}
	
	// Adds an Outgoing Connection
	public void addConnection(Device d) {
		connections.add(d);
  }
  
	/* inserts a rule to the top of the iptable of this device
	 */
	public void insertRule(String rule, Device source, String protocol) {
		Rule newRule = new Rule(rule, source, protocol);
		if (!hasRule(newRule))
			rules.add(0, newRule);
	}
	
	/* Appends a rule to the end of the iptable of this device
	 */
	public void appendRule(String rule, Device source, String protocol) {
		Rule newRule = new Rule(rule, source, protocol);
		if (!hasRule(newRule))
			rules.add(newRule);
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
        if (packet.getSource() == curRule.getSource() && 
            packet.getProtocol().equals(curRule.getProtocol())) {

          // TCP protocol
          if (packet.getProtocol().equals("TCP")) {

            // DROP (silently delete)
            if (curRule.getRule().equals("DROP")) {
              super.status = Status.Dead;

            // REJECT (notify of deletion)
            } else if (curRule.getRule().equals("REJECT")) {
              super.status = Status.Dead;
              // notify source device that it was rejected

            // ACCEPT, default
            } else {
              // notify source device that it was accepted
            }

          // UDP protocol
          } else if (packet.getProtocol().equals("UDP")) {

            // 25% chance of dropping
            if (Math.random() < 0.25) {
              super.status = Status.Dead;
              return;
            }

            // DROP (silently delete)
            if (curRule.getRule().equals("DROP")) {
              super.status = Status.Dead;

            // REJECT (silently delete)
            } else if (curRule.getRule().equals("REJECT")) {
              super.status = Status.Dead;

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
				scaledDeviceImg = device_image.getScaledCopy(
						(int)Simulation.Screen(7), (int)Simulation.Screen(7));
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
		get_image();
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
		g.setColor(new Color(211, 211, 211, 100));
		
		// Get direction to destination
		Vector direction = position.directionTo(d.position);
		float distance = position.distance(d.position);
		
		// Arrow End
		float arrowEndX = position.x + direction.x * distance * 0.975f;
		float arrowEndY = position.y + direction.y * distance * 0.975f;
		
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
	}
}