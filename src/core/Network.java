package core;

import java.util.ArrayList;

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
	
	// All packets in the network
	private ArrayList<Packet> packets;
	
	// Constructor
	public Network() {
		devices = new ArrayList<>();
		packets = new ArrayList<>();
	}
	
	// Add a Device
	public void addDevice(Device d) { devices.add(d); }
	// Add a Packet
	public void addPacket(Packet p) { packets.add(p); }
	
	// Send Packet Method - Selects 2 Nodes to Send a Packet Between
	public void sendPacket() {
		int rand1 = (int) (Math.random() * devices.size());
		int rand2 = (int) (Math.random() * 3);
		
		new Packet(
				devices.get(rand1), devices.get(rand1).randomConnection(rand2),
				"TCP", this);
	}
	
	// Update Method
	public void update() {
		// Update all Packets
		for ( Packet p : packets ) {
			p.update();
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
	
	// Removes a packet from the network if it were rejected/dropped 
	public void deletePacket(Packet packet) {
		System.out.println("Packet deleted");
		packets.remove(packet);
	}
}