package engine;

import engine.app.IGameInitializer;
import engine.app.config.AppLaunchConfig;
import engine.scene.Scene;
import engine.system.RequestManager;

/**
 * This game runner is ran by the engine to load update the game
 * 
 * @author Brandon Porter
 *
 */
class GameRunner {
	private static String ABORT_MSG = null;

	private final AppLaunchConfig _launchConfig;
	private final GameLoader _gameLoader;
	private final Display _activeDisplay;

	/**
	 * Constructs a game runner
	 * 
	 * @param gameInitializer
	 *            game-specific instance to load various scenes of the game
	 * @param launchConfig
	 *            set of config settings to initialize the engine with
	 */
	public GameRunner(Display display, IGameInitializer gameInitializer, AppLaunchConfig launchConfig) {
		this._activeDisplay = display;
		this._launchConfig = launchConfig;
		this._gameLoader = new GameLoader(gameInitializer);
	}

	/**
	 * Loads the game and starts the game loop
	 * 
	 * @throws Exception
	 */
	public void loadAndRun() throws Exception {
		// If a splash screen exists, it will load that first synchronously (we
		// will wait on it) and then load the game resources and the first scene
		// asynchronously
		_gameLoader.load(_activeDisplay, (errMsg) -> {
			if (errMsg != null)
				abort(errMsg);
		});

		// Starts the game loop
		run();
	}

	/**
	 * The main game loop
	 * 
	 * @throws Exception
	 */
	protected void run() throws Exception {
		float runTime = 0f;
		// TODO: Move interval to timer
		float interval = 1f / _launchConfig.maxUPS;

		// Keep going until the display says we should close
		// (i.e. they click the red x or closes it manually).
		// This will be updated later to handle game closing logic
		while (!_activeDisplay.shouldClose()) {
			// 1. Process user input
			processInput();

			// 2. Physics/AI fixed update logic
			runTime += TimeManager.getBenchmark();
			for (; runTime >= interval; runTime -= interval) {
				fixedUpdate();
			}

			// 3. All other update logic (once per frame)
			update();

			// 4. Draw to screen
			render();

			// TODO: Limit FPS if window is not V-Sync
		}
	}

	/**
	 * Update the global input states for last frame
	 */
	protected void processInput() {
		Input.updateInputState();
	}

	/**
	 * A fixed update could be called multiple times per frame and is determined
	 * by the max UPS (options set in settings).
	 * 
	 * This is where physics/AI logic should happen
	 */
	protected void fixedUpdate() {
		// TODO: ADD LOGIC HERE
	}

	/**
	 * Updates the active scene, called once per frame. This is where all other
	 * logic should happen
	 * 
	 * @throws Exception
	 */
	protected void update() throws Exception {
		// First thing we do is throw an exception if
		// one occurred
		if (ABORT_MSG != null)
			throw new Exception(ABORT_MSG);

		// If the game has decided to load a new scene,
		// we check that here first, switch to it if true,
		// and then update the main display projection matrix.
		if (SceneManager.hasNewScene())
			SceneManager.switchToNewScene(_activeDisplay);

		// Updates the currently active scene
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
			activeScene.update();
		}
	}

	/**
	 * Renders the active scene, called once per frame
	 * 
	 * @throws Exception
	 */
	protected void render() throws Exception {
		// Renders the currently active scene
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
			activeScene.render(_activeDisplay);
		}

		// Updates the display
		_activeDisplay.refresh();

		// Executes any outstanding OpenGL requests
		RequestManager.executeSomeGLRequests();
	}

	/**
	 * Done with the game, dispose any state
	 */
	protected void dispose() {
		_gameLoader.dispose();
	}

	/**
	 * Stops the game by throwing an exception with the specified message
	 * 
	 * @param abortMessage
	 *            the message that caused the runner to crash
	 */
	protected static void abort(String abortMessage) {
		ABORT_MSG = abortMessage;
	}
}
