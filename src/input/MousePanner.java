package input;

import org.newdawn.slick.Input;

import core.geometry.Vector;
import engine.Settings;
import engine.Simulation;

// Adjusts
public class MousePanner {
	private boolean panning;
	
	private float mouseX;
	private float mouseY;
	
	// Constructor
	public MousePanner() {
		// Initialize Variables
		panning = false;
		mouseX = mouseY = -1;
	}
	
	// Pan the Mouse
	public void pan(Input input, Vector center) {
		if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
			center.x -= (input.getMouseX() - mouseX) / Settings.Pixels_Per_Unit;
			center.y += (input.getMouseY() - mouseY) / Settings.Pixels_Per_Unit;
		}
		
		mouseX = input.getMouseX();
		mouseY = input.getMouseY();
	}
	
}