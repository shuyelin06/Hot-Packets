package core;

import org.newdawn.slick.Graphics;

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
	public enum Status { Alive, Dead }
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
		
		// Intialize Width and Height
		width = 2;
		height = 2;
		
		// Maintains Unique Identifier
		identifier = idCounter;
		idCounter++;
	}
	
	// Getters
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	
	// Gets the Device's Position
	public Vector getPosition() { return position.copy(); }
	// Get Device Identifier
	public int getID() { return identifier; }
	
	// Setters
	// Sets the Device's Status
	public void setStatus(Status status) { this.status = status; }
	
	// Helpers
	// Returns true if this device matches this identifier
	public boolean isObject(int identifier) {
		return this.identifier == identifier;
	}
	
	// Draw Method
	public abstract void draw(Graphics g);
	
}