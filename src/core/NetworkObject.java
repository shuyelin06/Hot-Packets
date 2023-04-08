package core;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import core.geometry.Vector;
import engine.Settings;

/*
 * NetworkObject Class:
 * Generic class representing all objects in the simulation
 * to be rendered.
 */
public abstract class NetworkObject {
	// Identifier Counter
	protected static int idCounter = 0;
	
	// Unique Identifier for an Object
	protected int identifier;
	
	// Status Variable
	public enum Status { Alive, Dead, Lost }
	protected Status status;
	
	// Position (Units)
	protected Vector position;
	
	// Width and Height (Units)
	protected float width, height;
	
	// Constructor
	public NetworkObject() {
		// Initialize Position
		position = new Vector();
		
		// Initialize Status
		status = Status.Alive;
		
		// Initialize Width and Height
		width = 2;
		height = 2;
		
		// Maintains Unique Identifier
		identifier = idCounter;
		idCounter++;
	}
	
	// Getters
	public Status getStatus() { return status; }
	
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
	// Gets an Array of Strings Decribing the Device
	public abstract void getInfo(ArrayList<String> info);
	
	// Gets the Device's Position
	public Vector getPosition() { return position.copy(); }
	// Get Device Identifier
	public int getID() { return identifier; }
	
	// Setters
	// Sets the Device's Status
	public void setStatus(Status status) { this.status = status; }
	
	// Helpers
	// Returns true if network object contains some coordinate
	public boolean atCoordinate(float x, float y) {
		return (position.x - width / 2 < x) && (x < position.x + width / 2)
				&& (position.y - height / 2 < y) && (y < position.y + height / 2);
	}
	
	// Returns true if this device matches this identifier
	public boolean isObject(int identifier) {
		return this.identifier == identifier;
	}
	
	// Draw Method
	public abstract void draw(Graphics g);
}