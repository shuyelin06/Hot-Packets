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
	
	// Constructor
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Copy Constructor
	public Vector(Vector vector) {
		x = vector.x;
		y = vector.y;
	}
	
	// Vector Magnitude
	public float magnitude() { return (float) Math.sqrt(x * x + y * y); };
	
	// Multiplies a Vector by a Scalar
	public Vector scalarMultiply(float scale) {
		return new Vector(x * scale, y * scale);
	}
	
	// Return a Copy of a Normalized Vector
	public Vector normalize() { 
		float magnitude = magnitude();
		return new Vector(x / magnitude, y / magnitude);
	}
	// Return a Copy of a Vector (Rotated Theta Counterclockwise)
	public Vector rotate(float theta) {
		return new Vector(
				(float) (x * Math.cos(theta) - y * Math.sin(theta)),
				(float) (x * Math.sin(theta) + y * Math.cos(theta))
				);
	}
	
	// Returns a Copy of the Vector
	public Vector copy() { return new Vector(this); }
	
	// Distance to Another Vector
	public float distance(Vector v2) {
		return (float) Math.sqrt((v2.x - x) * (v2.x - x) + 
							(v2.y - y) * (v2.y - y));
	}
	// Direction to Another Vector
	public Vector directionTo(Vector to) {
		return new Vector(to.x - x, to.y - y).normalize();
	}
	
	
}