package graphics.boxes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Settings;
import graphics.Box;

public class SliderBox extends Box {
	
	// Slider 1 - Tick Speed
	private Slider tickSlider; 
	
	// Slider 2 - Packet Speed
	private Slider packetSlider;
	
	// Constructor
	public SliderBox() {
		tickSlider = new Slider(0, 120f);
		tickSlider.setValue(Settings.Ticks_Per_Second);
		
		packetSlider = new Slider(0, 3f);
		packetSlider.setValue(Settings.Packet_Speed);
	}
	
	// Initialize: Sets the Sizes of the Sliders
	public void initialize() {
		tickSlider
			.setX(centerX)
			.setY(centerY + height * 0.15f)
			.setWidth(width * 0.825f)
			.setHeight(height * 0.15f);
		
		packetSlider
			.setX(centerX)
			.setY(centerY + height * 0.35f)
			.setWidth(width * 0.825f)
			.setHeight(height * 0.15f);
	}
	
	// Mouse click will move sliders
	public void mouseClick(float mouseX, float mouseY) {
		tickSlider.handleMouse(mouseX, mouseY);
		packetSlider.handleMouse(mouseX, mouseY);
		
		// Update Setting Variables
		Settings.Ticks_Per_Second = tickSlider.getValue();
		Settings.Packet_Speed = packetSlider.getValue();
	}
	
	// Draw Method
	public void draw(Graphics g) {
		super.draw(g);
		
		g.setColor(Color.black);
		g.drawString("Ticks / Second", centerX - g.getFont().getWidth("Ticks / Second") / 2, 
					centerY + height * 0.2f);	
		tickSlider.draw(g);
		
		g.setColor(Color.black);
		g.drawString("Packet Speed", centerX - g.getFont().getWidth("Packet Speed") / 2, 
				centerY + height * 0.4f);	
		packetSlider.draw(g);
		
	}
	 
}