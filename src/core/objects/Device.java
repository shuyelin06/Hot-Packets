package core.objects;

import java.util.ArrayList;
import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
	
	// Get Connections
	public ArrayList<Device> getConnections() { return connections; }
	
	// Constructor
	public Device(float x, float y) {
		super();
		
		// Set Postion
		position.x = x;
		position.y = y;
		
		// Initialize Variables
		connections = new ArrayList<>();
		
		name = "NULL";
		ip = "NULL";
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
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		// Draw Device (Circle)
		g.setColor(Color.blue);
		g.drawOval(
			Simulation.ScreenX(position.x - width / 2), 
			Simulation.ScreenY(position.y + height / 2), 
			Simulation.Screen(width), Simulation.Screen(height));
		
		// Draw Connection
		g.setColor(new Color(211, 211, 211, 100));
		for ( Device dest : connections ) {
			g.drawLine(
					Simulation.ScreenX(position.x), Simulation.ScreenY(position.y), 
					Simulation.ScreenX(dest.position.x), Simulation.ScreenY(dest.position.y));
		}
	}
	
}