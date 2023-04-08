package core;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import core.objects.Device;
import core.objects.Packet;

/* 
 * Graph Class:
 * 
 * Basic class representing a graph for the
 * simulation.
 */
public class Network {
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
	
	// Update Method
	public void update() {
		// Update all Packets
		for ( Packet p : packets ) {
			p.update();
		}
	}
	
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