

import engine.GameEngine;
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
		System.out.println("Starting Game Engine...");
		GameEngine engine = null;
		try {
			GameEngine.EngineOptions options = new GameEngine.EngineOptions();
			options.windowOptions.resizable = true;
			options.graphicsOptions.polygonMode = false;
			
			engine = new GameEngine(new GameInitializer(), GAME_TITLE, WIDTH, HEIGHT, options);
			engine.initializeAndRun();
		} catch (Exception e) {
			// TODO: Catch a specific exception, possibly implement a restart
			e.printStackTrace();
		} finally {
			// Clean up the engine saying we have finished
			if (engine != null) {
				engine.dispose();
			}
		}
		
		System.out.println("Stopping Game Engine...");
	}
}
