package game.scenes;

import java.util.List;

import engine.common.GameObject;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.scenes.SceneLoader;
import game.GameResources;

/**
 * Creates the application splash loader which will display while our app boots
 * 
 * @author Brandon Porter
 *
 */
public class SplashLoader extends SceneLoader {
	public static final String NAME = "Splash Scene";

	/**
	 * Creates a new splash scene loader
	 */
	public SplashLoader() {
		super(NAME);
	}

	/**
	 * Loads game objects for splash
	 */
	@Override
	protected List<GameObject> loadGameObjectsForScene() throws Exception {

		// Create mesh and set texture material
		Mesh mesh = GameResources.Meshes.CUBE;
		Material mat = new Material(GameResources.Textures.GRASS_BLOCK);

		return null;
	}

}
