package engine;

import engine.scenes.Scene;

/**
 * Our game display that represents a game window and graphics controller
 * 
 * @author brandon.porter
 *
 */
public class Display {
	/**
	 * The main display singleton
	 */
	protected static final Display MAIN = new Display();
	
	private Window _window;
	private GraphicsController _graphicsController;

	// Singleton class
	private Display() {
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
	protected void init(String title, int width, int height, Window.WindowOptions windowOptions,
			GraphicsController.GraphicsOptions graphicsOptions) {
		_window = new OpenGLWindow(title, width, height, windowOptions);
		_window.init();
		_window.setWindowResizedCallback(this::onWindowResized);

		_graphicsController = new OpenGLGraphicsController(graphicsOptions);
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
	 * 
	 * @return true/false if window resized
	 */
	public boolean hasResized() {
		boolean hasResized = _window.hasResized();
		_window.setResized(false);
		return hasResized;
	}

	/**
	 * Closes the window and exits the game
	 */
	public void closeAndExit() {
		_window.close();
	}

	/**
	 * Helper function to update the graphics viewport to the size of the window
	 */
	public void fixViewportToWindow() {
		getGraphicsController().setViewport(0, 0, _window.getWidthScaled(), _window.getHeightScaled());
	}

	/**
	 * Sets the new window title
	 * 
	 * @param newTitle
	 *            title to be displayed on the display window
	 */
	public void setNewTitle(String newTitle) {
		_window.setTitle(newTitle);
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
	 * @return the window associated to this display. Protected to be used for
	 *         internal purposes only
	 */
	protected Window getWindow() {
		return _window;
	}

	/**
	 * Renders the window, continually called from the game loop several times a
	 * second
	 */
	protected void refresh() {
		_window.refresh();
	}

	/**
	 * Updates the camera projection to the aspect ratio change of the window
	 */
	protected void updateCameraProjectionMatrix() {
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
	protected boolean shouldClose() {
		return _window.shouldClose();
	}

	/**
	 * Cleans up and destroys the game display
	 */
	protected void dispose() {
		_window.dispose();
	}

	/**
	 * Callback called when the window has been resized
	 */
	private void onWindowResized() {
		fixViewportToWindow();
		updateCameraProjectionMatrix();
	}
}
