package engine;

import engine.scenes.Scene;

/**
 * Our game display that represents a game window and graphics controller
 * 
 * @author brandon.porter
 *
 */
public final class GameDisplay {
	private static Window _window;
	private static GraphicsController _graphicsController;

	// Static class
	private GameDisplay() {
	}

	/**
	 * Creates and initializes the game window and graphics controller
	 * 
	 * @param title
	 *            text displayed on top of the window
	 * @param width
	 *            starting width of the window
	 * @param height
	 *            starting height of the window
	 * @param windowOptions
	 *            additional options to initialize the window
	 * @param graphicsOptions
	 *            additional options to initialize the graphics
	 */
	protected static void create(String title, int width, int height, Window.WindowOptions windowOptions,
			GraphicsController.GraphicsOptions graphicsOptions) {
		_window = new OpenGLWindow(title, width, height, windowOptions);
		_window.init();
		_window.setWindowResizedCallback(GameDisplay::onWindowResized);

		_graphicsController = new OpenGLGraphicsController(graphicsOptions);
		_graphicsController.init();
	}

	/**
	 * Shows the window
	 */
	public static void show() {
		_window.show();
	}

	/**
	 * Hides the window
	 */
	public static void hide() {
		_window.hide();
	}

	/**
	 * Checks if the window has been resized manually
	 * 
	 * @return true/false if window resized
	 */
	public static boolean hasResized() {
		boolean hasResized = _window.hasResized();
		_window.setResized(false);
		return hasResized;
	}

	/**
	 * Closes the window and exits the game
	 */
	public static void closeAndExit() {
		_window.close();
	}

	/**
	 * Helper function to update the graphics viewport to the size of the window
	 */
	public static void fixViewportToWindow() {
		getGraphicsController().setViewport(0, 0, _window.getWidthScaled(), _window.getHeightScaled());
	}

	/**
	 * Sets the new window title
	 * 
	 * @param newTitle
	 *            title to be displayed on the display window
	 */
	public static void setNewTitle(String newTitle) {
		_window.setTitle(newTitle);
	}

	/**
	 * Gets the graphics controller for this display
	 * 
	 * @return graphics controller
	 */
	public static GraphicsController getGraphicsController() {
		return _graphicsController;
	}

	/**
	 * @return the window associated to this display. Protected to be used for
	 *         internal purposes only
	 */
	protected static Window getWindow() {
		return _window;
	}

	/**
	 * Renders the window, continually called from the game loop several times a
	 * second
	 */
	protected static void refresh() {
		_window.refresh();
	}

	/**
	 * Updates the camera projection to the aspect ratio change of the window
	 */
	protected static void updateCameraProjectionMatrix() {
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null && activeScene.getCamera() != null) {
			float aspectRatio = (float) _window.getWidth() / (float) _window.getHeight();
			activeScene.getCamera().updateProjectionMatrix(aspectRatio);
		}
	}

	/**
	 * Checks if the window should close
	 * 
	 * @return true or false if the window is ready to close (e.g if the user
	 *         clicks x on the game)
	 */
	protected static boolean shouldClose() {
		return _window.shouldClose();
	}

	/**
	 * Cleans up and destroys the game display
	 */
	protected static void dispose() {
		_window.dispose();
	}

	/**
	 * Callback called when the window has been resized
	 */
	private static void onWindowResized() {
		fixViewportToWindow();
		updateCameraProjectionMatrix();
	}
}
