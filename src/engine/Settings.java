package engine;

public class Settings {
	// Screen Width and Height (Pixels)
	public static int Screen_Height = -1;
	public static int Screen_Width = -1;
	
	// Pixels per Unit
	// Number of pixels that represent a unit in the simulation
	public static float Pixels_Per_Unit = 10f;
	
	// Ticks per Second
	// A tick is defined as one simulation time frame.
	public static float Ticks_Per_Second = 60f;
	
	// Packet Speed
	// Amount of units a packet moves every tick
	public static float Packet_Speed = 0.45f;
	
	// Traffic Congestion
	// Amount of packets a device will route every tick.
	public static float Max_Traffic = 1.75f;
}

