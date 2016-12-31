package engine;

import java.lang.reflect.InvocationTargetException;

import engine.common.Defaults;
import engine.graphics.GraphicsController;
import engine.graphics.Window;
import engine.utils.Logger;

/**
 *
 * Controls the main loop of the game
 *
 * @author brandon.porter
 *
 */
public class GameEngine {
	private static final Logger _log = new Logger("GameEngine");

	private final GameManager _gameManager;
	private final Timer _timer;
	private final GameDisplay _display;
	private final EngineOptions _options;

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the game
	 */
	public GameEngine(IGameInitializer game) {
		this(game, Defaults.Window.TITLE);
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the game
	 * @param title
	 *            text displayed on the game window
	 */
	public GameEngine(IGameInitializer game, String title) {
		this(game, title, Defaults.Window.WIDTH, Defaults.Window.HEIGHT);
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the game
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 */
	public GameEngine(IGameInitializer game, String title, int width, int height) {
		this(game, title, width, height, new EngineOptions());
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param gameInitializer
	 *            instance of the initializer that loads the game
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 * @param options
	 *            set options to initialize the engine with
	 */
	public GameEngine(IGameInitializer gameInitializer, String title, int width, int height, EngineOptions options) {
		_log.debug("Created GameEngine");

		this._gameManager = new GameManager(gameInitializer);
		this._options = options;
		this._timer = new Timer();
		this._display = new GameDisplay(title, width, height, options.windowOptions, options.graphicsOptions);
	}

	/**
	 * Initializes the game instance and starts
	 * 
	 * @throws Exception
	 */
	public void initializeAndRun() throws Exception {
		// Initialize
		init();
		// Begin game loop
		run();
	}

	/**
	 * Cleans up the game engine
	 */
	public void dispose() {
		_log.debug("Disposing engine...");
		_gameManager.dispose();
		_display.dispose();
	}

	/**
	 * Initializes all the core components
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		_log.debug("Initializing engine...");
		// Init and show display
		_display.init();
		_display.show();
		// Clear the screen
		_display.getGraphicsController().clearColor(0, 0, 0, 0);
		// Init the game timer
		_timer.init();
		// Init your game
		_gameManager.init();
	}

	/**
	 * Game Loop
	 * 
	 * @throws Exception
	 */
	protected void run() throws Exception {
		float runTime = 0f;
		// TODO: Move interval to timer
		float interval = 1f / _options.maxUPS;

		_log.debug("Running engine...");

		while (!_display.shouldClose()) {
			// 1. Process user input
			processInput();

			// 2. Update game state
			runTime += _timer.getElapsedTime();
			for (; runTime >= interval; runTime -= interval) {
				update();
			}

			// 3. Render game
			render();

			// TODO: Limit FPS if window is not V-Sync
		}
	}

	/**
	 * Game processes input
	 */
	protected void processInput() {
		// TODO: Implement input processing
	}

	/**
	 * Update game state
	 * 
	 * @throws Exception
	 */
	protected void update() throws Exception {
		// TODO: Implement update
		_gameManager.update(_display);
	}

	/**
	 * Render updated game state to screen
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	protected void render() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// TODO: Implement rendering
		_gameManager.render(_display);
		_display.render();
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
