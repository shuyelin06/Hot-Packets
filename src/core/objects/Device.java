package core.objects;

import java.util.ArrayList;

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
	}
	
}