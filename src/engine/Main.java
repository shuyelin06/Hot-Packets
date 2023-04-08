package engine;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {
    // BasicGameStates 
    private BasicGameState example;
    
    private static AppGameContainer appgc;
    
    
	public Main(String name) { 
		super(name); 
		
		this.example = Simulation.getInstance();
	}

	public static int getScreenWidth() { return appgc.getScreenWidth(); }
	public static int getScreenHeight() { return appgc.getScreenHeight(); }
	
	public static int getFPS() { return appgc.getFPS(); }
	
	public void initStatesList(GameContainer gc) throws SlickException 
	{
		addState(example);
	}

	public static void main(String[] args) 
	{
		try 
		{
			appgc = new AppGameContainer(new Main("Bitcamp 2023"));
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			
			Settings.Screen_Width = appgc.getScreenWidth();
			Settings.Screen_Height = appgc.getScreenHeight();
			
			appgc.setDisplayMode(Settings.Screen_Width, Settings.Screen_Height, false);
			appgc.setTargetFrameRate(60);
			
			appgc.start();
			appgc.setVSync(true);
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
}