package engine.core;

import engine.app.IGameInitializer;
import engine.app.config.Defaults;
import engine.rendering.shader.ShaderInitializer;
import engine.system.RequestManager;

/**
 *
 * Manages and controls the runner that runs the main loop of the game
 *
 * @author brandon.porter
 *
 */
public class Engine {
	/**
	 * The name of the main thread this engine is running on
	 */
	public static final String MAIN_THREAD = Thread.currentThread().getName();

	private final GameRunner _gameRunner;

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the scenes for the game
	 * @throws Exception
	 */
	public Engine(IGameInitializer game) throws Exception {
		this(game, Defaults.Window.TITLE);
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the scenes for the game
	 * @param title
	 *            text displayed on the game window
	 * @throws Exception
	 */
	public Engine(IGameInitializer game, String title) throws Exception {
		this(game, title, Defaults.Window.WIDTH, Defaults.Window.HEIGHT);
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the scenes for the game
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 * @throws Exception
	 */
	public Engine(IGameInitializer game, String title, int width, int height) throws Exception {
		this(game, title, width, height, new EngineOptions());
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the scenes for the game
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 * @param options
	 *            set options to initialize the engine with
	 * @throws Exception
	 */
	public Engine(IGameInitializer gameInitializer, String title, int width, int height, EngineOptions options)
			throws Exception {
		// Create the display but don't show it
		getDisplay().init(title, width, height, options.windowOptions, options.graphicsOptions);

		// Initialize input
		Input.init(getDisplay().getWindow());

		// Initialize our graphics
		ShaderInitializer.init();

		// Initialize the scene manager with the game-specific scene loaders
		// We use this to determine what scenes to load when the game requests
		// one
		SceneManager.init(gameInitializer.getSceneLoaders());

		// Create the runner
		this._gameRunner = new GameRunner(getDisplay(), gameInitializer, options);
	}

	/**
	 * Starts the engine and runs the game
	 * 
	 * @throws Exception
	 *             Any exception caused in the game loop will be thrown here
	 */
	public void run() throws Exception {
		// Show and clear the screen
		getDisplay().show();
		getDisplay().getGraphicsController().clearColor(0, 0, 0, 0);

		// Start the game timer
		TimeManager.start();

		// Runs the main loop
		_gameRunner.loadAndRun();

		// If we get here the game has successfully run with no exceptions
		// and a close has been requested, so we dispose of the engine and
		// quit.
		//
		// EDIT: May implement exception handling on app side to determine
		// whether to continue
		// running the game or dispose
		//
		// dispose();
	}

	/**
	 * Cleans up all resources created by the game or engine
	 */
	public void dispose() {
		// Cleans up all our scenes
		SceneManager.dispose();
		// Cleans up our shaders & other graphics
		ShaderInitializer.dispose();
		// Cleans up our window and callbacks
		getDisplay().dispose();
		// Finish up all graphics requests created by disposing
		RequestManager.executeAllGLRequests();

		// Clean up our runner (and game specific resources)
		_gameRunner.dispose();
		// Finish remaining any remaining requests and disposes
		RequestManager.dispose();
	}

	/**
	 * Sets a runtime failure message, and alerts the game runner to escape on
	 * next game update frame
	 * 
	 * @param crashFailureMsg
	 *            the message that caused the crash
	 */
	public static void crash(String crashFailureMsg) {
		GameRunner.abort(crashFailureMsg);
	}

	/**
	 * Gets the active display, for now just returning the main until we support
	 * multiple
	 * 
	 * @return the display to launch and run the application on
	 */
	private Display getDisplay() {
		return Display.MAIN;
	}

	/**
	 * Defines the starting values to initialize the engine
	 * 
	 * @author brandon.porter
	 *
	 */
	public static class EngineOptions {
		/**
		 * The options for the window
		 */
		public final Window.WindowOptions windowOptions = new Window.WindowOptions();

		/**
		 * The options for the graphics
		 */
		public final GraphicsController.GraphicsOptions graphicsOptions = new GraphicsController.GraphicsOptions();

		/**
		 * The max frames per second to render the screen at
		 */
		public int maxFPS = Defaults.Engine.MAX_FPS;

		/**
		 * The max amount of times game state can be updated per second
		 */
		public int maxUPS = Defaults.Engine.MAX_UPS;
	}
}
