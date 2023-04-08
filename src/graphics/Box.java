package graphics;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Simulation;

public abstract class Box {
	// Center X and Y
	protected float centerX;
	protected float centerY;
	
	// Width and Height
	protected float width;
	protected float height;
	
	// Constructor
	public Box() {
		centerX = 0;
		centerY = 0;
		
		width = 0;
		height = 0;
	}
	
	// Initialize Method
	// Boxes can use this method to initialize their
	// related components and dependencies
	public void initialize() {}
	
	// Update Method
	// Boxes can use this method to update
	// if needed
	public void update() {}
	
	// Setters
	public Box setX(float x) { centerX = x; 		return this; }
	public Box setY(float y) { centerY = y; 		return this; }
	
	public Box setWidth(float w) { width = w; 		return this; }
	public Box setHeight(float h) { height = h; 	return this; }
	
	// Process Mouse Input
	public boolean handleMouse(float mouseX, float mouseY) {
		if ( (centerX - width / 2 < mouseX) && (mouseX < centerX + width / 2)
				&& (centerY - height / 2 < mouseY) && (mouseY < centerY + height / 2) ) {
			mouseClick(mouseX, mouseY);
			return true;
		}
		
		return false;
	}

	// Abstract Method - For Boxes to Inherit
	// Handles mouse input on boxes
	protected abstract void mouseClick(float mouseX, float mouseY);
	
	// Rendering
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(centerX - width / 2, centerY - height / 2, width, height);
	}
	
}