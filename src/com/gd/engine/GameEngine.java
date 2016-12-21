package com.gd.engine;

import com.gd.engine.graphics.GraphicsController;
import com.gd.engine.graphics.Window;
import com.gd.engine.graphics.WindowDefaults;

/**
 *
 * Controls the main loop of the game
 *
 * @author brandon.porter
 *
 */
public class GameEngine {
	private final IGame _game;
	private final GameDisplay _display;
	private final EngineOptions _options;

	/**
	 * Constructs the game engine
	 * 
	 * @param game
	 *            instance of the game to run on this engine
	 */
	public GameEngine(IGame game) {
		this(game, WindowDefaults.TITLE);
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param game
	 *            instance of the game to run on this engine
	 * @param title
	 *            text displayed on the game window
	 */
	public GameEngine(IGame game, String title) {
		this(game, title, WindowDefaults.WIDTH, WindowDefaults.HEIGHT);
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param game
	 *            instance of the game to run on this engine
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 */
	public GameEngine(IGame game, String title, int width, int height) {
		this(game, title, width, height, new EngineOptions());
	}

	/**
	 * Constructs the game engine
	 * 
	 * @param game
	 *            instance of the game to run on this engine
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 * @param options
	 *            set options to initialize the engine with
	 */
	public GameEngine(IGame game, String title, int width, int height, EngineOptions options) {
		this._game = game;
		this._options = options;
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
		_game.dispose();
		_display.dispose();
	}

	/**
	 * Initializes all the core components
	 */
	protected void init() {
		_display.init();
		_game.init();
	}

	/**
	 * Game Loop
	 */
	protected void run() {
		while (true) {
			_display.update();
		}
	}
	
	/**
	 * Defines the starting values to initialize the engine
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
		public int maxFPS = EngineDefaults.MAX_FPS;
		
		/**
		 * The max amount of times game state can be updated per second
		 */
		public int maxUPS = EngineDefaults.MAX_UPS;
	}
}
