package engine.scenes;

import java.util.LinkedHashMap;

/**
 * Manages the active scene in our game and helps load new scenes
 * 
 * @author Brandon Porter
 *
 */
public final class SceneManager {
	public static final SceneManager Instance = new SceneManager();

	protected final LinkedHashMap<String, SceneLoader> sceneLoaders = new LinkedHashMap<>();
	protected String activeScene = null;
	protected String loadingScene = null;

	// Singleton
	private SceneManager() {
	}

	/**
	 * Set the scene loaders to be used to load various scenes throughout the
	 * game
	 * 
	 * @param sceneLoaders
	 *            the scene loaders as specified by the game initializer
	 */
	public void setSceneLoaders(SceneLoader[] sceneLoaders) {
		this.sceneLoaders.clear();
		for (SceneLoader loader : sceneLoaders) {
			this.sceneLoaders.put(loader.sceneName, loader);
		}
	}

	/**
	 * Loads the scene specified the sceneName from our scene loader
	 * 
	 * @param sceneName
	 *            name of scene to load
	 * @throws Exception
	 */
	public static boolean loadScene(String sceneName) throws Exception {
		// This may not be a warning
		if (Instance.loadingScene != null) {
			// For now, let's dispose the loading scene
			// TODO: do it on a separate thread
			if (Instance.loadingScene != Instance.activeScene)
				Instance.sceneLoaders.get(Instance.loadingScene).clearScene();
		}

		// Get the new scene to load
		SceneLoader loader = Instance.sceneLoaders.get(sceneName);
		if (loader == null) {
			return false;
		}

		// Set this scene to be loading
		Instance.loadingScene = sceneName;

		// load and initialize the scene
		if (!loader.sceneIsLoaded()) {
			loader.loadScene().init();
		}

		return loader.sceneIsLoaded();
	}

	/**
	 * Loads the "next" scene compared to our current scene
	 * 
	 * @throws Exception
	 */
	public static boolean loadNextScene() throws Exception {
		String currScene = Instance.activeScene;
		String nextScene = null;

		// Iterate over the linkedHashMap to get the next scene
		boolean foundScene = false;
		for (String key : Instance.sceneLoaders.keySet()) {
			// We don't have an active scene, so get the first
			if (currScene == null || foundScene) {
				nextScene = key;
				break;
			}

			foundScene = key.equals(currScene);
		}

		// If we don't have another scene, return false
		if (nextScene == null) {
			return false;
		}

		// Found the scene, load it
		return loadScene(nextScene);
	}

	/**
	 * @return the currently active scene, null if none is currently active
	 */
	public static Scene getActiveScene() {
		if (Instance.activeScene == null)
			return null;

		return Instance.sceneLoaders.get(Instance.activeScene).getScene();
	}

	/**
	 * @return true or false if we currently have a scene ready to show
	 */
	public boolean newSceneReady() {
		return loadingScene != null && sceneLoaders.get(loadingScene).sceneIsLoaded();
	}

	/**
	 * Switches to the ready scene and disposes the old one if applicable
	 * 
	 * @param disposeOldScene
	 *            clears the old scene, otherwise keeps it in memory
	 */
	public void switchToLoadedScene(boolean disposeOldScene) {
		// Set the loading scene as active and start it
		String oldActiveScene = activeScene;
		this.activeScene = loadingScene;
		this.loadingScene = null;

		// The new scene is now active
		getActiveScene().onForeground();
		
		// We may not have an active scene
		if (oldActiveScene == null)
			return;
		
		// Dispose or background the old active scene
		SceneLoader oldScene = Instance.sceneLoaders.get(oldActiveScene);
		if (disposeOldScene)
			oldScene.clearScene();
		else
			oldScene.getScene().onBackground();
	}

	/**
	 * Disposes the active and ready scenes
	 */
	public void dispose() {
		for (SceneLoader loader : Instance.sceneLoaders.values()) {
			loader.clearScene();
		}
		Instance.sceneLoaders.clear();
	}
}
