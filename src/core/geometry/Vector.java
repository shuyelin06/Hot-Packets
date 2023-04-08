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
	
	// Distance between Two Vectors
	public static float Distance(Vector v1, Vector v2) {
		return (float) Math.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y));
	}
	// Vector Difference between Two Vectors
	public static Vector VectorDifference(Vector from, Vector to) {
		return new Vector(to.x - from.x, to.y - from.y);
	}
	
	// Multiplies a Vector by a Scalar
	public Vector scalarMultiply(float scale) {
		return new Vector(x * scale, y * scale);
	}
	
	// Return a Copy of a Normalized Vector
	public Vector normalize() { 
		float magnitude = magnitude();
		return new Vector(x / magnitude, y / magnitude);
	}
	
	// Returns a Copy of the Vector
	public Vector copy() { return new Vector(this); }
}