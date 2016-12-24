package engine;

/**
 * Our game display that represents a game window and graphics controller
 * 
 * @author brandon.porter
 *
 */
public class GameDisplay {
	private final Window _window;
	private final GraphicsController _graphicsController;

	/**
	 * Constructs a game display
	 * 
	 * @param title
	 * @param width
	 * @param height
	 */
	public GameDisplay(String title, int width, int height) {
		this(title, width, height, new Window.WindowOptions());
	}

	/**
	 * Constructs a game display
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @param windowOptions
	 */
	public GameDisplay(String title, int width, int height, Window.WindowOptions windowOptions) {
		this(title, width, height, windowOptions, new GraphicsController.GraphicsOptions());
	}

	/**
	 * Constructs a game display
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @param graphicsOptions
	 */
	public GameDisplay(String title, int width, int height, GraphicsController.GraphicsOptions graphicsOptions) {
		this(title, width, height, new Window.WindowOptions(), graphicsOptions);
	}

	/**
	 * Constructs a game display
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @param windowOptions
	 * @param graphicsOptions
	 */
	public GameDisplay(String title, int width, int height, Window.WindowOptions windowOptions,
			GraphicsController.GraphicsOptions graphicsOptions) {
		this._window = new OpenGLWindow(title, width, height, windowOptions);
		this._graphicsController = new OpenGLGraphicsController(graphicsOptions);
	}

	/**
	 * Initializes the display window and graphics controller
	 */
	public void init() {
		_window.init();
		_graphicsController.init();
	}

	/**
	 * Shows the window
	 */
	public void show() {
		_window.show();
	}

	/**
	 * Hides the window
	 */
	public void hide() {
		_window.hide();
	}

	/**
	 * Checks if the window has been resized manually
	 * @return true/false if window resized
	 */
	public boolean hasResized() {
		return _window.hasResized();
	}
	
	/**
	 * Tell the window that the game has finished updating state to be resized
	 */
	public void resetResized() {
		_window.setResized(false);
	}
	
	/**
	 * Renders the window, continually called from the game loop several times a
	 * second
	 */
	public void render() {
		_window.render();
	}

	/**
	 * Closes the window and exits the game
	 */
	public void closeAndExit() {
		_window.close();
	}

	/**
	 * Checks if the window should close
	 * 
	 * @return true or false if the window is ready to close (e.g if the user
	 *         clicks x on the game)
	 */
	public boolean shouldClose() {
		return _window.shouldClose();
	}
	
	/**
	 * Helper function to update the graphics viewport to the size of the window
	 */
	public void fixViewportToWindow() {
		getGraphicsController().setViewport(0, 0, _window.getWidthScaled(), _window.getHeightScaled());
	}
	
	/**
	 * Helper function to update the graphics viewport ONLY IF the window has been resized (more performant)
	 */
	public void fixViewportToWindowIfResized() {
		if (hasResized()) {
			fixViewportToWindow();
			resetResized();
		}
	}

	/**
	 * Gets the graphics controller for this display
	 * 
	 * @return graphics controller
	 */
	public GraphicsController getGraphicsController() {
		return _graphicsController;
	}

	/**
	 * Cleans up and destroys the game display
	 */
	public void dispose() {
		_window.dispose();
	}
}
