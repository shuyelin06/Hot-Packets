package core;

import org.newdawn.slick.Graphics;

import core.geometry.Vector;

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
	
	// Position
	protected Vector position;
	
	// Constructor
	public NetworkObject() {
		// Maintains Unique Identifier
		identifier = idCounter;
		idCounter++;
	}
	
	// Getters
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