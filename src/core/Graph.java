package core;

import java.util.ArrayList;

import core.objects.Device;
import core.objects.Packet;

/* 
 * Graph Class:
 * 
 * Basic class representing a graph for the
 * simulation.
 */
public class Graph {
	// All devices in the network
	private ArrayList<Device> devices;
	
	// All packets in the network
	private ArrayList<Packet> packets;
}