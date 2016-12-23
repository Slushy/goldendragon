package com.gd.engine.graphics;

/**
 * Base class for controlling the graphics on displays
 * 
 * @author brandon.porter
 *
 */
public abstract class GraphicsController {
	protected final GraphicsOptions graphicsOptions;
	
	/**
	 * Construct a graphics controller
	 * 
	 * @param graphicsOptions
	 *            additional options to initialize the graphics
	 */
	public GraphicsController(GraphicsOptions graphicsOptions) {
		this.graphicsOptions = graphicsOptions;
	}

	/**
	 * Initializes the graphics for the display
	 */
	public abstract void init();

	/**
	 * Clears the display window of any graphics with the specified color
	 * @param r 0-1 value (1 for full red, 0 for no red)
	 * @param g 0-1 value (1 for full green, 0 for no green)
	 * @param b 0-1 value (1 for full blue, 0 for no blue)
	 * @param a 0-1 value (1 for fully transparent, 0 for no transparency)
	 */
	public abstract void clearColor(float r, float g, float b, float a);
	
	/**
	 * Additional options to initialize the graphics
	 * 
	 * @author brandon.porter
	 *
	 */
	public static class GraphicsOptions {
	}
}
