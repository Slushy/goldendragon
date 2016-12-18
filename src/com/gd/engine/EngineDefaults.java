package com.gd.engine;

/**
 * Defines default values to be consumed by the game engine
 * @author brandon.porter
 *
 */
public class EngineDefaults {
	/**
	 * The default text displayed as the title on the window
	 */
	public static final String TITLE = "Golden Dragon - Game Engine";
	
	/**
	 * The default width of the game window
	 */
	public static final int WIDTH = 1280;
	
	/**
	 * The default height of the game window
	 */
	public static final int HEIGHT = 720;
	
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
	private EngineDefaults() {}
}
