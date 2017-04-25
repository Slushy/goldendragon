package game.scenes.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.common.GameObject;
import engine.graphics.MeshMaterial;
import engine.graphics.components.MeshRenderer;
import engine.resources.loaders.MeshLoader;
import engine.resources.loaders.TextureLoader;
import engine.scenes.ApplicationSplashLoader;
import game.GameResources;
import game.scenes.SplashSceneBehavior;

/**
 * Creates the application splash loader which will display while our app boots
 * 
 * @author Brandon Porter
 *
 */
public class SplashLoader extends ApplicationSplashLoader {
	public static final String NAME = "Splash Scene";

	private static final double MIN_TIME_DISPLAYED_MS = -1;

	/**
	 * Creates a new splash scene loader
	 */
	public SplashLoader() {
		super(NAME, MIN_TIME_DISPLAYED_MS);
	}

	/**
	 * Loads game objects for splash
	 */
	@Override
	protected List<GameObject> loadGameObjectsForScene() throws Exception {
		this.loadResourcesForSceneSync();
		
		// Set a cube in middle of screen slanted
		GameObject cube = new GameObject("Cube");
		cube.addComponent(
				new MeshRenderer(GameResources.Meshes.CUBE, new MeshMaterial(GameResources.Textures.GRASS_BLOCK)));
		cube.getTransform().setPosition(0, 0.2f, -5);
		cube.getTransform().setRotX(-45);

		// Create our splash screen script
		GameObject script = new GameObject("Splash script");
		script.addComponent(new SplashSceneBehavior());

		return new ArrayList<>(Arrays.asList(cube, script));
	}

	// Loads the resources for the splash synchronously. This should be as
	// bare-bone as possible so we can get a splash displayed quickly
	private void loadResourcesForSceneSync() throws Exception {
		// Load cube mesh and textures
		MeshLoader.loadMesh(GameResources.Meshes.CUBE, GameResources.Meshes.CUBE.getName());
		TextureLoader.loadTexture(GameResources.Textures.GRASS_BLOCK, GameResources.Textures.GRASS_BLOCK.getName());
	}
}
