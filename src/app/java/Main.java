

import engine.core.Engine;
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
			Engine.EngineOptions options = new Engine.EngineOptions();
			options.windowOptions.resizable = true;
			options.windowOptions.showFPS = true;
			options.windowOptions.vSync = true;
			options.graphicsOptions.polygonMode = false;
			
			engine = new Engine(new GameInitializer(), GAME_TITLE, WIDTH, HEIGHT, options);
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
