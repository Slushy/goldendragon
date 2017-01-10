package engine;

import engine.Engine.EngineOptions;
import engine.resources.RequestManager;
import engine.scenes.Scene;

/**
 * This game runner is ran by the engine to load update the game
 * 
 * @author Brandon Porter
 *
 */
class GameRunner {
	private final EngineOptions _options;
	private final GameLoader _gameLoader;

	/**
	 * Constructs a game runner
	 * 
	 * @param gameInitializer
	 *            game-specific instance to load various scenes of the game
	 * @param options
	 *            set options to initialize the engine with
	 */
	public GameRunner(IGameInitializer gameInitializer, EngineOptions options) {
		this._options = options;
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
		_gameLoader.load((errMsg) -> {
			if (errMsg != null)
				Engine.runtimeFailureMsg = errMsg;
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
		float interval = 1f / _options.maxUPS;

		// Keep going until the display says we should close
		// (i.e. they click the red x or closes it manually).
		// This will be updated later to handle game closing logic
		while (!Display.MAIN.shouldClose()) {
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
		if (Engine.runtimeFailureMsg != null)
			throw new Exception(Engine.runtimeFailureMsg);

		// If the game has decided to load a new scene,
		// we check that here first, switch to it if true,
		// and then update the main display projection matrix.
		if (SceneManager.hasNewScene())
			SceneManager.switchToNewScene();

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
			activeScene.render();
		}

		// Updates the display
		Display.MAIN.refresh();

		// Executes any outstanding OpenGL requests
		RequestManager.executeSomeGLRequests();
	}

	/**
	 * Done with the game, dispose any state
	 */
	protected void dispose() {
		_gameLoader.dispose();
	}
}
