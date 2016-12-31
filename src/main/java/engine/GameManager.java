package engine;

import java.lang.reflect.InvocationTargetException;

import engine.scenes.Scene;
import engine.scenes.SceneManager;
import engine.utils.Logger;

/**
 * This private game manager is used by the engine to initialize, update, and
 * render the game
 * 
 * @author Brandon Porter
 *
 */
class GameManager {
	private static final Logger _log = new Logger("SceneManager", Logger.LoggerLevel.DEBUG);

	private IGameInitializer _gameInitializer;
	private GameLoadedStatus _loadStatus = GameLoadedStatus.LOADING;

	/**
	 * Constructs a game manager
	 * 
	 * @param gameInitializer
	 *            game specific initializer
	 */
	public GameManager(IGameInitializer gameInitializer) {
		this._gameInitializer = gameInitializer;
		// Sets the scene loaders
		SceneManager.Instance.setSceneLoaders(gameInitializer.getSceneLoaders());
	}

	/**
	 * First begins by loading the scene manager and showing the application
	 * splash as quickly as possible
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		// First thing is first, load splash screen
		Scene appSplash = _gameInitializer.getApplicationSplash();
		if (appSplash != null) {
			_log.debug("Showing application splash: %s", appSplash.getName());
		}

		// Begin loading game in separate thread
		Thread gameLoader = new Thread(() -> {
			try {
				// Load game resources
				_gameInitializer.loadResources();
				// Begin loading next scene
				SceneManager.loadNextScene();

				// Done
				this._loadStatus = GameLoadedStatus.DONE;
			} catch (Exception e) {
				this._loadStatus = GameLoadedStatus.ERROR;
				this._loadStatus.setException(e);
			} finally {
				this.loadingComplete();
			}
		});

		gameLoader.run();
		// gameLoader.start() -> Need to create request processing
	}

	/**
	 * Updates the active scene, called once per frame
	 * 
	 * @param display
	 * @throws Exception
	 */
	public void update(GameDisplay display) throws Exception {
		// Check if there was an error loading
		if (_loadStatus == GameLoadedStatus.ERROR) {
			throw new Exception("The game failed to load: ", _loadStatus.getException());
		}

		// Check the scene manager if we should show a new scene
		if (SceneManager.Instance.newSceneReady()) {
			_log.debug("Switched to loaded scene");
			SceneManager.Instance.switchToLoadedScene(true);
			display.updateCameraProjectionMatrix();
		}

		// Updates the active scene
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
			activeScene.update(display.getInputHandler());
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
	public void render(GameDisplay display) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		// Renders the currently active scene
		Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
			activeScene.render(display.getGraphicsController());
		}
	}

	/**
	 * Done with the game, dispose any state
	 */
	public void dispose() {
		SceneManager.Instance.dispose();
	}

	/*
	 * Called when game loading has been finished
	 */
	private void loadingComplete() {
		this._gameInitializer = null;
	}

	/**
	 * Temporary enum to determine the game loaded status, eventually we will
	 * have a full state machine to determine this globally.
	 * 
	 * @author Brandon Porter
	 *
	 */
	private static enum GameLoadedStatus {
		LOADING, DONE, ERROR;

		private Exception _ex;

		/**
		 * @return the exception caused during game loading
		 */
		public Exception getException() {
			return _ex;
		}

		/**
		 * Sets the loading exception
		 * 
		 * @param ex
		 */
		public void setException(Exception ex) {
			this._ex = ex;
		}
	}
}
