package core;

import java.util.ArrayList;
import java.util.Stack;

import org.newdawn.slick.Graphics;

import core.NetworkObject.Status;
import core.objects.Device;
import core.objects.Packet;

/* 
 * Graph Class:
 * 
 * Basic class representing a graph for the
 * simulation.
 */
public class Network {
	private static Network network = null;
	
	// Network Call
	public static Network getInstance() {
		if ( network == null ) {
			network = new Network();
		}
		return network;
	}
	
	// All devices in the network
	private ArrayList<Device> devices;
	
	// Stack of new devices to be added
	private Stack<Device> newDevices;
	
	// All packets in the network
	private ArrayList<Packet> packets;
	
	// Stack of new packets to be added
	private Stack<Packet> newPackets;
	
	// Constructor
	public Network() {
		devices = new ArrayList<>();
		packets = new ArrayList<>();
		
		newPackets = new Stack<>();
		newDevices = new Stack<>();
	}
	
	// Add a Device
	public void addDevice(Device d) { 
		newDevices.push(d); 
		devices.add(newDevices.pop()); 
	}
	
	// Add a Packet
	public void addPacket(Packet p) { newPackets.push(p); }
	
	// Search Object Method - Searches for a NetworkObject Matching
	// specific game corodinates
	public NetworkObject searchForObject(float x, float y) {
		for ( Device d : devices ) {
			if ( d.getStatus() == Status.Alive && d.atCoordinate(x, y) )
				return d;
		}
		
		for ( Packet p : packets ) {
			if ( p.getStatus() == Status.Alive && p.atCoordinate(x, y ) )
				return p;
		}
		
		return null;
	}
	
	
	// Send Packet Method - Selects 2 Nodes to Send a Packet Between
	public void sendPacket() {
		int rand1 = (int) (Math.random() * devices.size());
		int rand2 = (int) (Math.random() * 4 + 1);
		
		Device source = devices.get(rand1);
		Device destination = source.randomConnection(rand2);
		if ( source != destination ) {
			new Packet(source, destination, Packet.Protocol.TCP);
		}
		
	}
	
	// Update Method
	public void update() {
		while (!newPackets.isEmpty()) {
			packets.add(newPackets.pop());
		}
			
		// Update all Packets
		for ( Packet p : packets ) {
			p.update();
		}
		for ( Device d : devices ) {
			d.update();
		}
		
		// Clean-Up Dead Packets and Devices
		packets.removeIf(p -> (p.status == Status.Dead));
		devices.removeIf(d -> (d.status == Status.Dead));
	} // anna is so cool
	
	// Draw Method
	public void draw(Graphics g) {
		// Draws all Devices
		// Note: Devices will draw their own outgoing edges
		for ( Device d : devices ) {
			d.draw(g);
		}
		
		// Draws all Packets
		for ( Packet p : packets ) {
			p.draw(g);
		}
	}
	

}