package com.gd.engine.graphics;

/**
 * Defines default values for the game window
 * 
 * @author brandon.porter
 *
 */
public class WindowDefaults {
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
	 * Whether the window can be resized by default
	 */
	public static final boolean RESIZABLE = false;

	/**
	 * Whether to limit the GPU to output frames as high as the refresh rate of
	 * the monitor. If enabled it prevents screen tearing but can also introduce
	 * minor input lag.
	 */
	public static final boolean VSYNC = true;

	/*
	 * Prevent outside classes from creating an instance
	 */
	private WindowDefaults() {
	}
}
