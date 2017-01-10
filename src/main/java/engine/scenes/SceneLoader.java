package engine.scenes;

import java.util.List;

import engine.common.GameObject;

/**
 * A scene loader is what the scene manager uses as a lookup table when trying
 * to find a scene to load. This will be overridden in the game since each game
 * will have their own scenes.
 * 
 * @author Brandon Porter
 *
 */
public abstract class SceneLoader {
	public final String sceneName;

	/**
	 * The scene to load, this will be null if the scene has not been created
	 * yet
	 */
	private Scene _scene = null;

	/**
	 * Constructs a new scene loader
	 */
	public SceneLoader(String sceneName) {
		this.sceneName = sceneName;
	}

	/**
	 * Loads the scene by setting the initial game objects from the dynamic game
	 * object loader
	 * 
	 * @return whether or not the scene was loaded successfully
	 * @throws Exception
	 */
	protected boolean loadScene() throws Exception {
		// clear the existing scene if it exists
		clearScene();

		// Create a new scene
		this._scene = new Scene(sceneName);

		// loads the game objects for the scene and adds them
		// to the scene
		for (GameObject obj : loadGameObjectsForScene())
			_scene.addGameObject(obj);

		/**
		 * Initializes the scene
		 */
		_scene.init();

		return _scene.isReady();
	}

	/**
	 * Disposes the scene and sets it to null
	 */
	protected synchronized void clearScene() {
		if (_scene != null) {
			_scene.dispose();
			_scene = null;
		}
	}

	/**
	 * @return the scene to be shown
	 */
	protected final Scene getScene() {
		return _scene;
	}

	/**
	 * Loads the game objects for the specific scene
	 * 
	 * @return the loaded game objects
	 * @throws Exception
	 */
	protected abstract List<GameObject> loadGameObjectsForScene() throws Exception;
}
