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
	 * Additional options to initialize the graphics
	 * 
	 * @author brandon.porter
	 *
	 */
	public static class GraphicsOptions {
	}
}
