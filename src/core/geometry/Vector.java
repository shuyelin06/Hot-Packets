package core.geometry;

/* 
 * Vector Class:
 * Represents any arbitrary position
 * in the simulation world.
 */
public class Vector {
	// Position x
	public float x;
	// Position y
	public float y;
	
	// Default Constructor
	public Vector() {
		x = y = 0f;
	}
	
	// Copy Constructor
	public Vector(Vector vector) {
		x = vector.x;
		y = vector.y;
	}
	
	// Returns a Copy of the Vector
	public Vector copy() { return new Vector(this); }
}