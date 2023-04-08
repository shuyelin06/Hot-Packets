package graphics.boxes;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.NetworkObject;
import engine.Simulation;
import graphics.Box;

public class InfoBox extends Box {
	
	// Reference to Selected Entity
	private NetworkObject selected;
	
	// ArrayList of Strings Describing Entity
	private ArrayList<String> info;
	
	/* Settings for InfoBox Text Rendering */
	private static float textPadding = 10f;
	
	// Constructor
	public InfoBox() {
		selected = null;
		
		info = new ArrayList<String>();
	}
	
	// Set Selected
	public void setSelected(NetworkObject o) { selected = o; }
	
	// Draw Method
	public void draw(Graphics g) {
		if ( selected != null ) {
			// Clear Info List
			info.clear();
			
			// Draw Box
			super.draw(g);
			
			// Render Text
			g.setColor(Color.black);
			selected.getInfo(info);
			
			renderText(g, info);
			
			// Circle NetworkObject
			g.setColor(Color.yellow);
			float size = (float) Math.sqrt(
						selected.getWidth() * selected.getWidth() + selected.getHeight() * selected.getHeight());
			g.drawOval(
				Simulation.ScreenX(selected.getX() - size / 2), Simulation.ScreenY(selected.getY() + size / 2), 
				Simulation.Screen(size), Simulation.Screen(size));	
		}
		
	}
	
	// Renders a list of strings, each of which will be rendered on 
	// a separate line
	private void renderText(Graphics g, ArrayList<String> strings) {
		float x = centerX - width / 2 + textPadding;
		float y = centerY - height / 2 + textPadding;
		
		for ( String s : strings ) {
			g.drawString(s, x, y);
			y += g.getFont().getHeight(s) + textPadding / 4;
		}
	}
	 
}