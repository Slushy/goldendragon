package com.gd.engine;

/**
 *
 * Controls the main loop of the game
 *
 * @author brandon.porter
 *
 */
public class GameEngine {
	private static final String DEFAULT_TITLE = "Golden Dragon - Game Engine";
	private static final int DEFAULT_WIDTH = 1280;
	private static final int DEFAULT_HEIGHT = 720;

	private final IGame _game;
	private final GameWindow _gameWindow;

	/**
	 * Constructs the game engine
	 * 
	 * @param game
	 *            instance of the game to run on this engine
	 */
	public GameEngine(IGame game) {
		this(game, DEFAULT_TITLE);
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
		this(game, title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
		this._game = game;
		this._gameWindow = new GameWindow(title, width, height);
		// TODO: Create GameWindow and pass in constructor values
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
		_gameWindow.dispose();
	}

	/**
	 * Initializes all the core components
	 */
	protected void init() {
		_gameWindow.init();
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
