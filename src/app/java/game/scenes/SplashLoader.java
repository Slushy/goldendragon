package game.scenes;

import java.util.List;

import engine.common.GameObject;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.scenes.SceneLoader;
import game.GameResources;

public class SplashLoader extends SceneLoader {
	public static final String NAME = "Splash Scene";

	public SplashLoader() {
		super(NAME);
	}

	@Override
	protected List<GameObject> loadGameObjectsForScene() throws Exception {

		// Create mesh and set texture material
		Mesh mesh = GameResources.Meshes.CUBE;
		Material mat = new Material(GameResources.Textures.GRASS_BLOCK);

		return null;
	}

}
