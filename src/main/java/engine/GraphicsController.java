package engine;

import engine.app.config.GraphicsConfig;

/**
 * Base class for controlling the graphics on displays
 * 
 * @author brandon.porter
 *
 */
public abstract class GraphicsController {
	protected final GraphicsConfig graphicsConfig;
	protected boolean inPolygonMode = false;
	
	/**
	 * Construct a graphics controller
	 * 
	 * @param graphicsConfig
	 *            additional configuration options to initialize the graphics
	 */
	protected GraphicsController(GraphicsConfig graphicsConfig) {
		this.graphicsConfig = graphicsConfig;
	}

	/**
	 * Initializes the graphics for the display
	 */
	public abstract void init();

	/**
	 * Clears the display window of any graphics with the specified color
	 * 
	 * @param r
	 *            0-1 value (1 for full red, 0 for no red)
	 * @param g
	 *            0-1 value (1 for full green, 0 for no green)
	 * @param b
	 *            0-1 value (1 for full blue, 0 for no blue)
	 * @param a
	 *            0-1 value (1 for fully transparent, 0 for no transparency)
	 */
	public abstract void clearColor(float r, float g, float b, float a);

	/**
	 * Clears the graphics for another render frame TODO: MORE INFORMATION
	 */
	public abstract void clearGraphics();

	/**
	 * Clears the graphics for another render frame
	 * 
	 * @param bits
	 *            the bits of graphics to clear TODO: MORE INFORMATION
	 */
	public abstract void clearGraphics(int bits);

	/**
	 * Resets the viewport to the specified size
	 * 
	 * @param x
	 *            x point on the screen
	 * @param y
	 *            y point on the screen
	 * @param width
	 *            width of the viewport
	 * @param height
	 *            height of the viewport
	 */
	public abstract void setViewport(int x, int y, int width, int height);

	/**
	 * Enables/disables polygon mode
	 * 
	 * @param polygonMode true to enable polygon mode, false to disable
	 */
	public void setPolygonMode(boolean polygonMode) {
		this.inPolygonMode = polygonMode;
	}
	
	/**
	 * @return true if we are in polygon mode, false if not
	 */
	public boolean inPolygonMode() {
		return inPolygonMode;
	}
}
