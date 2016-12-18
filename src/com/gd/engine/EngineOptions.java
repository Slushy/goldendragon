package com.gd.engine;

/**
 * Defines the starting values to initialize the engine
 * @author brandon.porter
 *
 */
public class EngineOptions {
	/**
	 * The max frames per second to render the screen at
	 */
	public int maxFPS = EngineDefaults.MAX_FPS;
	
	/**
	 * The max amount of times game state can be updated per second
	 */
	public int maxUPS = EngineDefaults.MAX_UPS;
}
