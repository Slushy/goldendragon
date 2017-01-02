package engine;

import java.lang.reflect.InvocationTargetException;

import engine.resources.RequestManager;
import engine.scenes.Scene;
import engine.scenes.SceneHandler;
import engine.scenes.SceneLoader;
import engine.utils.Debug;

/**
 * This private game manager is used by the engine to initialize, update, and
 * render the game
 * 
 * @author Brandon Porter
 *
 */
class GameManager {
	/**
	 * Quick way of detecting state, definitely not permanent
	 */
	public static GameState STATE = GameState.NOT_STARTED;

	private IGameInitializer _gameInitializer;
	private String _firstScene;

	/**
	 * Constructs a game manager
	 * 
	 * @param gameInitializer
	 *            game specific initializer
	 */
	public GameManager(IGameInitializer gameInitializer) {
		this._gameInitializer = gameInitializer;
	}

	/**
	 * First begins by loading the scene manager and showing the application
	 * splash as quickly as possible
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		STATE = GameState.INITIALIZING;

		// Initialize our graphics first
		GraphicsManager.init();

		// Initialize the scene manager
		SceneLoader[] sceneLoaders = _gameInitializer.getSceneLoaders();
		this._firstScene = sceneLoaders[0].sceneName;
		SceneManager.init(sceneLoaders);

		// We are initialized, lets begin loading
		this.load();
	}

	/**
	 * Updates the active scene, called once per frame
	 * 
	 * @param display
	 * @throws Exception
	 */
	protected void update() throws Exception {
		// Checks the game state before any execution
		this.checkGameState();

		// Updates the active scene
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
			activeScene.update();
		}
	}

	/**
	 * Renders the active scene, called once per frame
	 * 
	 * @param display
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	protected void render() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		// Renders the currently active scene
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
			activeScene.render();
		}
	}

	/*
	 * Loads and shows the application splash and then begins to load any other
	 * game resources
	 */
	private void load() {
		STATE = GameState.LOADING_SPLASH;

		// First thing is first, load splash screen (Should be synchronous so we
		// can wait on it)
		SceneLoader appSplashLoader = _gameInitializer.getApplicationSplashLoader();
		if (appSplashLoader != null) {
			// TODO: Show app splash
		}

		// Begin loading game resources in separate thread
		STATE = GameState.LOADING_RESOURCES;
		RequestManager.makeResourceRequest(() -> {
			try {
				_gameInitializer.loadResourcesAsync();

				// Wait for all additional resource and graphics requests to
				// complete before we begin loading the scene
				RequestManager.waitForAllRequestsOnSeparateThread(() -> {
					STATE = GameState.LOADING_RESOURCES_COMPLETE;
				});

			} catch (Exception e) {
				STATE = GameState.FAILURE;
				Debug.error("Error loading game resources");
				e.printStackTrace();
			}
		});
	}

	/*
	 * Checks the game state every update frame. TODO: Move to a game state
	 * manager eventually, only here temporarily.
	 */
	private void checkGameState() throws Exception {
		switch (STATE) {
		case FAILURE:
			throw new Exception("The game failed to load: ", STATE.Exception());
		case LOADING_RESOURCES_COMPLETE:
			STATE = GameState.LOADING_FIRST_SCENE;
			SceneManager.loadSceneAsync(_firstScene, (success) -> {
				if (success) {
					STATE = GameState.FIRST_SCENE_READY;
					Debug.log("Scene is ready");
				}
				else {
					STATE = GameState.FAILURE;
					STATE.setException(new Exception("Couldn't load game scene so just saying we failed"));
				}
			});
			break;
		case FIRST_SCENE_READY:
			STATE = GameState.RUNNING;
			SceneManager.switchToNewScene();
			GameDisplay.updateCameraProjectionMatrix();
			break;
		default:
			break;
		}
	}

	/**
	 * Done with the game, dispose any state
	 */
	protected void dispose() {
		SceneManager.dispose();
	}
}
