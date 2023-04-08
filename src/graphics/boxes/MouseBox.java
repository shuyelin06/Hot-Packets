package graphics.boxes;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import core.geometry.Vector;
import engine.Settings;
import engine.Simulation;
import graphics.Box;

public class MouseBox extends Box {
	
	private Input input;
	
	private Vector center;
	
	private float lastMouseX;
	private float lastMouseY;
	
	public MouseBox(Input input) {
		// Obtain Simulation Input
		this.input = input;
		
		// Initialize Variables
		lastMouseX = lastMouseY = -1;
		
		// Get Simulation Center
		center = Simulation.getInstance().getCenter();
	}
	
	// Do not draw anything
	@Override
	public void draw(Graphics g) {}

	// Update Internal Mouse Position
	@Override
	public void update() {
		lastMouseX = input.getMouseX();
		lastMouseY = input.getMouseY();
	}
		
	// Handle Mouse Panning
	@Override
	protected void mouseClick(float mouseX, float mouseY) {
		center.x -= (mouseX - lastMouseX) / Settings.Pixels_Per_Unit;
		center.y += (mouseY - lastMouseY) / Settings.Pixels_Per_Unit;
	}
	
}