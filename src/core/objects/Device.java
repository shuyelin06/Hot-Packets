package core.objects;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.NetworkObject;
import core.geometry.Vector;
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
		
		// Set Position2	
		position.x = x;
		position.y = y;
		
		// Initialize Variables
		connections = new ArrayList<>();
		
		name = "NULL";
		ip = "NULL";
		
		width = 7;
		height = 7;
	}
	
	// Protocol Method
	// Performs a protocol on a packet that has reached it
	public void protocol(Packet packet) {
		
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
		
	}
}