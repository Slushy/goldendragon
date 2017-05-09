package engine.core;

import java.util.LinkedHashMap;

import engine.app.ApplicationSplashLoader;
import engine.app.SceneLoader;
import engine.scene.Scene;
import engine.system.RequestManager;

/**
 * Acts as a wrapper around the scene loaders for the scene manager to
 * load/navigate scenes
 * 
 * @author Brandon Porter
 *
 */
public final class SceneHandler {
	private final LinkedHashMap<String, SceneLoader> _sceneLoaders = new LinkedHashMap<>();

	private String _activeScene = null;
	private String _loadingScene = null;

	/**
	 * Constructs an empty scene handler
	 */
	public SceneHandler() {
	}

	/**
	 * Sets the scene loaders to be used to load various scenes throughout the
	 * game
	 * 
	 * @param sceneLoaders
	 *            the scene loaders as specified by the game initializer
	 * @throws Exception
	 */
	public void initSceneLoaders(SceneLoader[] sceneLoaders) throws Exception {
		for (SceneLoader loader : sceneLoaders) {
			// Protect against duplicate scene names
			if (_sceneLoaders.containsKey(loader.sceneName)) {
				throw new Exception("Each scene laoder must have a unique name. Duplicate scene: " + loader.sceneName);
			}
			this._sceneLoaders.put(loader.sceneName, loader);
		}
	}

	/**
	 * @return true if we are currently loading a scene
	 */
	public boolean hasLoadingScene() {
		return _loadingScene != null;
	}

	/**
	 * Gets a scene instance by name, or null if it doesn't exist
	 * 
	 * @param name
	 *            name of the scene
	 * @return an existing scene
	 */
	public Scene getSceneByName(String name) {
		SceneLoader sceneLoader = _sceneLoaders.get(name);
		return sceneLoader != null ? sceneLoader.getScene() : null;
	}

	/**
	 * Stores the splash loader in memory, and loads the splash scene
	 * 
	 * @param loader
	 *            the scene loader to begin loading
	 * @return true or false if scene has loaded
	 * @throws Exception
	 */
	public boolean loadSplash(ApplicationSplashLoader splashLoader) throws Exception {
		// Add loader to memory
		_sceneLoaders.put(splashLoader.sceneName, splashLoader);
		// Set as the loading scene
		this._loadingScene = splashLoader.sceneName;
		// Load the scene and return successful
		return splashLoader.loadScene();
	}

	/**
	 * Loads the scene identified by sceneName by executing its loader
	 * 
	 * @param sceneName
	 *            name of the scene to load
	 * @return true or false if scene has loaded (if false it is most likely
	 *         async)
	 * @throws Exception
	 */
	public synchronized boolean loadScene(String sceneName) throws Exception {
		SceneLoader loader = _sceneLoaders.get(sceneName);
		if (loader == null)
			return false;

		// Set as the loading scene
		this._loadingScene = sceneName;

		// Load the scene and return successful
		return loader.loadScene();
	}

	/**
	 * @return the currently active scene, or null if there is none
	 */
	public Scene getActiveScene() {
		if (_activeScene == null)
			return null;
		return getSceneByName(_activeScene);
	}

	/**
	 * Assumes there is already a loaded scene and we just simply have it switch
	 * places with the currently active one. We return the name of the active
	 * scene (null if there was no active scene).
	 * 
	 * @return name of the old active scene (before this new one), or null if it
	 *         doesnt exist. It should only not exist when we are switching to
	 *         the very first scene of the game.
	 */
	public String setLoadingToActive() {
		String oldActive = _activeScene;
		_activeScene = _loadingScene;
		_loadingScene = null;

		return oldActive;
	}

	/**
	 * Disposes the scene by name
	 * 
	 * @param name
	 *            name of the scene to dispose
	 */
	public void disposeScene(String name) {
		SceneLoader sceneLoader = _sceneLoaders.get(name);
		if (sceneLoader != null)
			sceneLoader.clearScene();
	}

	/**
	 * Disposes the scene by name in a separate thread
	 * 
	 * @param name
	 *            name of the scene to dispose asynchronously
	 */
	public void disposeSceneAsync(String name) {
		SceneLoader sceneLoader = _sceneLoaders.get(name);
		if (sceneLoader != null) {
			RequestManager.makeResourceRequest(() -> {
				sceneLoader.clearScene();
			});
		}
	}

	/**
	 * Disposes the currently active scene
	 */
	public void disposeActiveScene() {
		disposeScene(_activeScene);
	}

	/**
	 * Disposes the entire scene handler and every loader
	 */
	public void dispose() {
		// Clear remaining scenes
		for (SceneLoader loader : _sceneLoaders.values()) {
			loader.clearScene();
		}
		_sceneLoaders.clear();
	}
}
