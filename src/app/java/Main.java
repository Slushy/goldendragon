

import engine.Engine;
import engine.app.config.AppLaunchConfig;
import engine.utils.Debug;
import game.GameInitializer;

/*
 * Entry point for running the GD game engine by itself
 */
public class Main {
	private static final String GAME_TITLE = "Engine Tester";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	/*
	 * Entry point 
	 */
	public static void main(String[] args) {
		Debug.ENABLED = true;
		Debug.log("Starting Game Engine...");
		
		Engine engine = null;
		try {
			AppLaunchConfig config = new AppLaunchConfig();
			config.window.resizable = true;
			config.window.showFPS = true;
			config.window.vSync = true;
			config.graphics.polygonMode = false;
			
			engine = new Engine(new GameInitializer(), GAME_TITLE, WIDTH, HEIGHT, config);
			engine.run();
		} catch (Exception e) {
			// TODO: Catch a specific exception, possibly implement a restart
			e.printStackTrace();
		} finally {
			// Clean up the engine saying we have finished
			if (engine != null) {
				engine.dispose();
			}
		}
		
		Debug.log("Stopping Game Engine...");
	}
}
