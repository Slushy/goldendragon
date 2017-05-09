package engine.app;

import java.util.List;

import engine.scene.GameObject;
import engine.scene.Scene;

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
	public final boolean loadScene() throws Exception {
		// clear the existing scene if it exists
		clearScene();

		// Create a new scene with any game objects
		if (_scene == null)
			this._scene = new Scene(sceneName, loadGameObjectsForScene());
		
		return _scene.init();
	}

	/**
	 * Disposes the scene and sets it to null
	 */
	public final synchronized void clearScene() {
		if (_scene != null && !shouldRetainSceneState()) {
			_scene.dispose();
			_scene = null;
		}
	}

	/**
	 * @return the scene to be shown
	 */
	public final Scene getScene() {
		return _scene;
	}

	/**
	 * Checks to see if we should retain the scene state instead of disposing.
	 * Defaults to false, and should be overridden if the particular scene wants
	 * to retain any scene state.
	 * 
	 * @return true if the scene should be disposed, false otherwise.
	 */
	protected boolean shouldRetainSceneState() {
		return false;
	}

	/**
	 * Loads the game objects for the specific scene
	 * 
	 * @return the loaded game objects
	 * @throws Exception
	 */
	protected abstract List<GameObject> loadGameObjectsForScene() throws Exception;
}
