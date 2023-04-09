package graphics.boxes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import engine.Simulation;
import graphics.Box;

public class EscapeBox extends Box {

	// Device image (same for every device object)
	private static Image device_image;
	private GameContainer arg0;
	
	public EscapeBox(GameContainer arg0) {
		super();
		this.arg0 = arg0;
	}
	
	@Override
	protected void mouseClick(float mouseX, float mouseY) {
		arg0.exit();
	}
	
	
	// Instantiate device image
	public static Image get_image() {
		if ( device_image == null ) {
			try {
				device_image = new Image("res/red_x.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		return device_image;
	}
	
	
	
	@Override
	public void draw(Graphics g) {
		Image scaledDeviceImg = get_image().getScaledCopy((int)width - 8, (int)height - 8);
		g.drawImage(scaledDeviceImg, 0, 0);
		
	}

}
