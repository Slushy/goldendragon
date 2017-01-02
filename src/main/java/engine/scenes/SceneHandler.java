package engine.scenes;

import java.util.LinkedHashMap;

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
	 */
	public void initSceneLoaders(SceneLoader[] sceneLoaders) {
		for (SceneLoader loader : sceneLoaders) {
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

	public Scene getActiveScene() {
		if (_activeScene == null)
			return null;
		return getSceneByName(_activeScene);
	}

	public String setLoadingToActive() {
		String oldActive = _activeScene;
		_activeScene = _loadingScene;
		_loadingScene = null;

		return oldActive;
	}

	public void disposeScene(String name) {
		SceneLoader sceneLoader = _sceneLoaders.get(name);
		if (sceneLoader != null)
			sceneLoader.clearScene();
	}

	public void disposeLoadingScene() {
		disposeScene(_loadingScene);
	}

	public void disposeActiveScene() {
		disposeScene(_activeScene);
	}

	public void dispose() {
		for (SceneLoader loader : _sceneLoaders.values()) {
			loader.clearScene();
		}
		_sceneLoaders.clear();
	}
}
