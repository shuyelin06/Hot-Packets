package graphics;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import engine.Settings;
import engine.Simulation;

/* 
 * Message Board Class
 * Contains messages to be displayed above a network entity,
 * which will gradually disappear over time.
 */
public class MessageBoard {
	// Maximum Messages to Display
	private static int Message_Cap = 6;
	
	// Max Time for a Message to Last
	private static float Max_Timer = 5f;
	
	// Color
	private Color color;
	
	// Timer (for Message Removal)
	private long lastRemoved;
	
	// Message Queue
	private PriorityQueue<String> queue;
	
	// Constructor
	public MessageBoard(Color color) {
		this.color = color;
		queue = new PriorityQueue<>();
		
		lastRemoved = 0;
	}
	
	// Add Message
	public void addMessage(String s) {
		if ( queue.size() == 0 ) {
			lastRemoved = System.currentTimeMillis();
		}
		
		if ( queue.size() < 6 ) {
			queue.add(s);
		}
	}
	
	// Update - Removes a Message When Timer Reaches 0
	public void update() {
		if ( queue.size() != 0 ) {
			if ( (System.currentTimeMillis() - lastRemoved) / 1000 >  
			Max_Timer / queue.size() ) {
				queue.poll();
				lastRemoved = System.currentTimeMillis();
			}
		}
		
	}
	
	// Render Messages at some coordinate (upwards)
	public void render(Graphics g, float x, float y) {
		// Green Message Color
		g.setColor(color);
		
		// Iterate from Newest (Queue Front) to Oldest
		// Render Messages Upwards
		float yOffset = 0;
		
		// Count Messages Rendered
		int count = 0; 
		
		for ( String s : queue ) {
			if ( count == Message_Cap ) break;
			
			g.scale(Settings.Pixels_Per_Unit / 20f, Settings.Pixels_Per_Unit / 20f);
			g.drawString(s, x * 20f / Settings.Pixels_Per_Unit, y * 20f / Settings.Pixels_Per_Unit + yOffset);
			g.scale(20f / Settings.Pixels_Per_Unit, 20f / Settings.Pixels_Per_Unit);
			
			yOffset -= g.getFont().getHeight(s);
			
			count++;
		}
		
	}
	
}