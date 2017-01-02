package engine;

import java.util.function.Consumer;

import engine.resources.RequestManager;
import engine.scenes.Scene;
import engine.scenes.SceneHandler;
import engine.scenes.SceneLoader;
import engine.utils.Debug;

/**
 * Manages the active scene in our game and helps load new scenes
 * 
 * @author Brandon Porter
 *
 */
public final class SceneManager {
	private static final SceneHandler _sceneHandler = new SceneHandler();

	// Static class
	private SceneManager() {
	}

	/**
	 * Initializes the handler that wraps the scene loading logic, should be
	 * assigned by our game manager
	 * 
	 * @param sceneLoaders
	 *            the scene loaders as specified by the game initializer
	 */
	protected static void init(SceneLoader[] sceneLoaders) {
		_sceneHandler.initSceneLoaders(sceneLoaders);
	}

	/**
	 * Loads the scene specified the sceneName from our scene loader
	 * 
	 * @param sceneName
	 *            name of scene to load
	 * @return boolean stating whether the scene has been loaded
	 * @throws Exception
	 */
	public static boolean loadScene(String sceneName) throws Exception {
		// This may not be a warning
		if (_sceneHandler.hasLoadingScene()) {
			// For now, let's log and return
			Debug.error("Trying to load scene " + sceneName + " while another scene is already loading");
			return false;
		}

		// Load the scene
		if (!_sceneHandler.loadScene(sceneName)) {
			throw new Exception("Scene failed to load");
		}

		return true;
	}

	/**
	 * Attempts to load the scene asynchronously
	 * 
	 * @param sceneName
	 *            name of scene to load
	 * @param onComplete
	 *            callback accept a boolean stating whether or not the scene was
	 *            successfully loaded
	 */
	public static void loadSceneAsync(String sceneName, Consumer<Boolean> onComplete) {
		// Attempt to load the scene asynchronously
		RequestManager.makeResourceRequest(() -> {
			try {
				// Execute the complete callback;
				onComplete.accept(loadScene(sceneName));

			} catch (Exception e) {
				Debug.error("Failed to load " + sceneName + " asynchronously");
				GameManager.STATE = GameState.FAILURE;
				GameManager.STATE.setException(e);
			}
		});
	}

	/**
	 * @return the currently active scene, null if none is currently active
	 */
	public static Scene getActiveScene() {
		return _sceneHandler.getActiveScene();
	}

	/**
	 * Switches to the ready scene and disposes the old one
	 */
	protected static void switchToNewScene() {
		if (!_sceneHandler.hasLoadingScene()) {
			Debug.error("Trying to switch to new scene when there is no loaded scene");
			return;
		}
		
		// Set the loading scene as active and start it
		String oldActiveScene = _sceneHandler.setLoadingToActive();
		
		// The new scene is now active
		getActiveScene().onForeground();

		// If we had an old active scene, dispose it
		if (oldActiveScene != null)
			_sceneHandler.disposeScene(oldActiveScene);
	}

	/**
	 * Disposes the active and ready scenes
	 */
	protected static void dispose() {
		_sceneHandler.dispose();
	}
}