package com.gd.engine;

/**
 *
 * Controls the main loop of the game
 *
 * @author brandon.porter
 *
 */
public class GameEngine {
	private final IGame _game;
	private final GameWindow _window;
	private final EngineOptions _options;

	/**
	 * Constructs the game engine
	 * 
	 * @param game
	 *            instance of the game to run on this engine
	 */
	public GameEngine(IGame game) {
		this(game, EngineDefaults.TITLE);
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
		this(game, title, EngineDefaults.WIDTH, EngineDefaults.HEIGHT);
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
		this._window = new GameWindow(title, width, height);
		this._options = options;
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
		_window.dispose();
	}

	/**
	 * Initializes all the core components
	 */
	protected void init() {
		_window.init();
		_game.init();
	}

	/**
	 * Game Loop
	 */
	protected void run() {

		while (true) {
		}
	}
}
