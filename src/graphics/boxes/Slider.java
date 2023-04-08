package graphics.boxes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import graphics.Box;

public class Slider extends Box {
	// Slider Size
	public static float Slider_Size = 5f;
	
	// Slider Minimum and Maximum
	private float min, max;
	
	// Stores the Slider's Value
	private float value;
	
	// Constructor
	public Slider(float min, float max) {
		// Set the Slider Minimum and Maximum
		this.min = min;
		this.max = max;
	}
	
	// Set Slider Value
	public void setValue(float value) { this.value = value; }
	
	// Get Slider Value
	public float getValue() { return value; }
	
	// Handle Mouse Click
	protected void mouseClick(float mouseX, float mouseY) {
		// If mouse is inside slider, change the slider's value 
		// proportional to the mouse location
		value = (mouseX - (centerX - width / 2)) / width * (max - min);
		
		if ( value > max ) value = max;
	}
	
	// Draw Method
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setLineWidth(3f);
		
		// Left Slider Bar Line
		g.drawLine(centerX - width / 2 - 1.5f, centerY - height / 4, 
					centerX - width / 2 - 1.5f, centerY + height / 4);
		// Right Slider Bar Line
		g.drawLine(centerX + width / 2 + 1.5f, centerY - height / 4, 
				centerX + width / 2 + 1.5f, centerY + height / 4);
		// Middle Slider Bar Line
		g.drawLine(centerX - width / 2 + 1.5f, centerY, 
				centerX + width / 2 - 1.5f, centerY);
		
		// Slider Box
		g.setColor(Color.red);
		g.fillRect(centerX - width / 2 + width * value / (max - min), 
				centerY - height / 4, Slider_Size, height / 2);
	}
}