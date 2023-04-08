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
	
	// Slider 3 - Zoom
	private Slider zoom; 
	
	// Constructor
	public SliderBox() {
		tickSlider = new Slider(0, 120f);
		tickSlider.setValue(Settings.Ticks_Per_Second);
		
		packetSlider = new Slider(0, 3f);
		packetSlider.setValue(Settings.Packet_Speed);
		
		zoom = new Slider(1f, 15f);
		zoom.setValue(Settings.Pixels_Per_Unit);
	}
	
	// Initialize: Sets the Sizes of the Sliders
	public void initialize() {
		zoom
			.setX(centerX)
			.setY(centerY - height * 0.1f)
			.setWidth(width * 0.825f)
			.setHeight(height * 0.15f);
		
		tickSlider
			.setX(centerX)
			.setY(centerY + height * 0.1f)
			.setWidth(width * 0.825f)
			.setHeight(height * 0.15f);
		
		packetSlider
			.setX(centerX)
			.setY(centerY + height * 0.3f)
			.setWidth(width * 0.825f)
			.setHeight(height * 0.15f);
		
		
	}
	
	// Mouse click will move sliders
	public void mouseClick(float mouseX, float mouseY) {
		zoom.handleMouse(mouseX, mouseY);
		tickSlider.handleMouse(mouseX, mouseY);
		packetSlider.handleMouse(mouseX, mouseY);
		
		// Update Setting Variables
		Settings.Pixels_Per_Unit = zoom.getValue();
		Settings.Ticks_Per_Second = tickSlider.getValue();
		Settings.Packet_Speed = packetSlider.getValue();
	}
	
	// Draw Method
	public void draw(Graphics g) {
		super.draw(g);
		
		g.setColor(Color.black);
		g.drawString("Press P", centerX - g.getFont().getWidth("Press P") / 2, 
				centerY - height * 0.45f);
		g.drawString("to send packets", centerX - g.getFont().getWidth("to send Packets") / 2, 
				centerY - height * 0.35f);
		
		g.setColor(Color.black);
		g.drawString("Zoom", centerX - g.getFont().getWidth("Zoom") / 2, 
				centerY - height * 0.05f);
		zoom.draw(g);
		
		g.setColor(Color.black);
		g.drawString("Ticks / Second", centerX - g.getFont().getWidth("Ticks / Second") / 2, 
					centerY + height * 0.15f);	
		tickSlider.draw(g);
		
		g.setColor(Color.black);
		g.drawString("Packet Speed", centerX - g.getFont().getWidth("Packet Speed") / 2, 
				centerY + height * 0.35f);	
		packetSlider.draw(g);
		
	}
	 
}