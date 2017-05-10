package engine;

import java.util.function.Consumer;

import engine.app.ApplicationSplashLoader;
import engine.app.SceneLoader;
import engine.scene.Scene;
import engine.system.RequestManager;
import engine.system.Timer;
import engine.utils.Debug;

/**
 * Static api to dynamically load new scenes throughout our game
 * 
 * @author Brandon Porter
 *
 */
public final class SceneManager {
	private static final SceneHandler _sceneHandler = new SceneHandler();
	private static final Timer _splashTimer = new Timer();

	private static boolean _newSceneLoaded = false;

	// NOTE: We should probably make the scene manager a singleton since we seem
	// to be throwing more and more state into it
	// Static class
	private SceneManager() {
	}

	/**
	 * Loads the scene specified the sceneName from our scene loader
	 * 
	 * @param sceneName
	 *            name of scene to load
	 * @return boolean stating whether the scene has been loaded
	 * @throws Exception
	 */
	public static synchronized boolean loadScene(String sceneName) throws Exception {
		// This may not be a warning
		if (_sceneHandler.hasLoadingScene()) {
			// For now, let's log and return
			Debug.error("Trying to load scene " + sceneName + " while another scene is already loading");
			return false;
		}

		// Load the scene
		_newSceneLoaded = _sceneHandler.loadScene(sceneName);
		if (!_newSceneLoaded) {
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
				e.printStackTrace();
				Engine.crash("Failed to load " + sceneName + " asynchronously");
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
	 * Initializes the handler that wraps the scene loading logic, should be
	 * assigned by our game manager
	 * 
	 * @param sceneLoaders
	 *            the scene loaders as specified by the game initializer
	 * @throws Exception
	 */
	protected static void init(SceneLoader[] sceneLoaders) throws Exception {
		_sceneHandler.initSceneLoaders(sceneLoaders);
	}

	/**
	 * Loads and shows the application splash for the game
	 * 
	 * @param display
	 * @param sceneLoader
	 * @return true or false if the splash was loaded and shown successfully
	 * @throws Exception
	 */
	protected static boolean loadAndShowApplicationSplash(Display display, ApplicationSplashLoader splashLoader) throws Exception {
		// This may not be a warning
		if (_sceneHandler.hasLoadingScene()) {
			// For now, let's log and return
			Debug.error("Trying to load the application splash while another scene is already loading");
			return false;
		}

		// Load the splash scene
		_newSceneLoaded = _sceneHandler.loadSplash(splashLoader);
		if (!_newSceneLoaded) {
			throw new Exception("Application splash scene failed to load");
		}

		// Try to switch to the new splash
		if (!switchToNewScene(display))
			return false;

		// If applicable, start a timer that will allow the splash scene
		// to be shown for a minimum amount of time before another scene
		// can be shown.
		double splashWait = splashLoader.getMinimumTimeSplashIsDisplayed();
		if (splashWait > 0.0) {
			Debug.log(String.format("Starting a splash timer for %.2f ms", splashWait));
			_splashTimer.start(splashWait);
		}

		return true;
	}

	/**
	 * @return true if we have a new scene to show
	 */
	protected static boolean hasNewScene() {
		// If we have a splash timer that has been started, make sure it is done
		// before a new scene can be loaded
		return _sceneHandler.hasLoadingScene() && _newSceneLoaded && !waitingOnSplashTimer();
	}

	/**
	 * Switches to the ready scene and disposes the old one
	 * 
	 * @return true if the scene was successfully switched to and is currently
	 *         showing
	 */
	protected static boolean switchToNewScene(Display display) {
		if (!hasNewScene()) {
			Debug.error("Trying to switch to new scene when there is no loaded scene");
			return false;
		}

		// We dont want to show the new scene yet if we still have an active
		// splash timer
		if (waitingOnSplashTimer()) {
			Debug.warn("Trying to switch to new scene when we have an active splash timer");
			return false;
		}
		// Game loop uses this boolean to dictate whether or not we should
		// switch, so set it to false until we have a new scene
		_newSceneLoaded = false;

		// Set the loading scene as active and start it
		String oldActiveScene = _sceneHandler.setLoadingToActive();

		// Set the camera projection matrix for the new main scene
		// NOTE: May not need this anymore since we only have one camera
		// although might be good practice to leave this here in case
		// we want to support multiple
		display.updateCameraProjectionMatrix();

		// The new scene is now active
		getActiveScene().start(display);

		// If we had an old active scene, dispose it asynchronously (so
		// our new scene is not waiting for it to unload)
		if (oldActiveScene != null)
			_sceneHandler.disposeSceneAsync(oldActiveScene);

		return true;
	}

	/**
	 * Disposes the active and ready scenes
	 */
	protected static void dispose() {
		_sceneHandler.dispose();
	}

	/**
	 * Checks if we are waiting for a splash to be finished showing
	 * 
	 * @return true if we have started a splash timer that is not finished,
	 *         otherwise if there is no splash timer or it has finished loading
	 *         we return false
	 */
	private static boolean waitingOnSplashTimer() {
		return _splashTimer.hasStarted() && !_splashTimer.isDone();
	}
}
