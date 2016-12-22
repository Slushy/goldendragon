package com.gd.engine;

/**
 * Defines default values to be consumed by the game engine
 * 
 * @author brandon.porter
 *
 */
public class EngineDefaults {
	/**
	 * The default max frames per second
	 */
	public static final int MAX_FPS = 60;

	/**
	 * The default max updates per second
	 */
	public static final int MAX_UPS = 30;

	/*
	 * Prevent outside classes from creating an instance
	 */
	private EngineDefaults() {
	}
}
