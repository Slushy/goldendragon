package com.gd;

import com.gd.engine.GameEngine;

/*
 * Entry point for running the GD game engine by itself
 */
public class Main {
	private static final String GAME_TITLE = "Engine Tester";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	public static void main(String[] args) {
		System.out.println("Starting Game Engine...");
		GameEngine engine = null;
		
		try {
			engine = new GameEngine(new Game(), GAME_TITLE, WIDTH, HEIGHT);
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
	}

}
