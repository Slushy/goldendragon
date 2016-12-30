package engine;

import engine.common.components.Camera;
import engine.graphics.GraphicsController;
import engine.graphics.OpenGLGraphicsController;
import engine.graphics.OpenGLWindow;
import engine.graphics.Window;
import engine.input.InputHandler;
import engine.utils.Logger;

/**
 * Our game display that represents a game window and graphics controller
 * 
 * @author brandon.porter
 *
 */
public class GameDisplay {
	private static final Logger _log = new Logger("GameDisplay");
	
	private final Window _window;
	private final GraphicsController _graphicsController;
	private final InputHandler _inputHandler;
	
	private Camera _camera;
	

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
		this._inputHandler = new InputHandler(_window);
	}

	/**
	 * Initializes the display window and graphics controller
	 */
	public void init() {
		_window.init();
		_window.setWindowResizedCallback(this::onWindowResized);
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
	 * Sets the camera as the default camera for the display
	 * 
	 * @param camera
	 */
	public void registerCamera(Camera camera) {
		this._camera = camera;
		updateCameraProjectionMatrix();
	}

	/**
	 * Gets the camera being used for the current display
	 * 
	 * @return current camera
	 */
	public Camera getCamera() {
		return _camera;
	}

	/**
	 * Checks if the window has been resized manually
	 * 
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

	/**
	 * Callback called when the window has been resized
	 */
	protected void onWindowResized() {
		_log.debug("Window is resized");
		fixViewportToWindow();
		updateCameraProjectionMatrix();
	}
	
	/**
	 * Updates the camera projection to the aspect ratio change of the window
	 */
	protected void updateCameraProjectionMatrix() {
		if (_camera != null) {
			float aspectRatio = (float) _window.getWidth() / (float) _window.getHeight();
			_camera.updateProjectionMatrix(aspectRatio);
		}
	}
	
	/**
	 * Gets the input handler, to be used by the engine to pass to the game
	 * @return input handler for the display
	 */
	public InputHandler getInputHandler() {
		return _inputHandler;
	}
}
