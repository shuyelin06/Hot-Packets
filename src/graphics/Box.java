package graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Simulation;

public class Box {
	// Center X and Y
	private float centerX;
	private float centerY;
	
	// Width and Height
	private float width;
	private float height;
	
	// Constructor
	public Box() {
		centerX = 0;
		centerY = 0;
		
		width = 0;
		height = 0;
	}
	
	// Setters
	public Box setX(float x) { centerX = x; 		return this; }
	public Box setY(float y) { centerY = y; 		return this; }
	
	public Box setWidth(float w) { width = w; 		return this; }
	public Box setHeight(float h) { height = h; 	return this; }
	
	// Returns True if Mouse is In Box
	public boolean mouseInBox(float mouseX, float mouseY) {
		return (centerX - width / 2 < mouseX) && (mouseX < centerX + width / 2)
			&& (centerY - height / 2 < mouseY) && (mouseY < centerY + height / 2);
	}
	
	// Rendering
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(centerX - width / 2, centerY - height / 2, width, height);
		g.setColor(Color.black);
		g.drawString("Testing", centerX, centerY);
	}
}