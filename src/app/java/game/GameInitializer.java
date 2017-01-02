package game;

import engine.IGameInitializer;
import engine.common.GameObject;
import engine.graphics.StandardShaderProgram;
import engine.graphics.components.MeshRenderer;
import engine.resources.RequestManager;
import engine.resources.loaders.MeshLoader;
import engine.resources.loaders.TextureLoader;
import engine.scenes.Scene;
import engine.scenes.SceneLoader;
import engine.utils.Debug;
import game.scenes.TestSceneLoader;

/**
 * Defines how our game will initialize
 * 
 * @author Brandon Porter
 *
 */
public class GameInitializer implements IGameInitializer {
	private boolean gameIsReady = false;

	@Override
	public SceneLoader getApplicationSplashLoader() {
		// Scene splash = new Scene("Test");
		//
		// try {
		// MeshLoader.loadMesh(GameResources.Meshes.CUBE);
		// TextureLoader.loadTexture(GameResources.Textures.GRASS_BLOCK);
		// } catch (Exception e) {
		// Debug.error("Error trying to load application splash");
		// e.printStackTrace();
		// }
		//
		// GameObject cube = new GameObject("Cube");
		// cube.addComponent(new MeshRenderer(mesh, mat));
		// cube.getTransform().setPosZ(-5);
		//
		// return splash;
		return null;
	}

	/**
	 * Called by the engine to load the game resources (in a separate thread)
	 * 
	 * @throws Exception
	 */
	@Override
	public void loadResourcesAsync() throws Exception {
		Debug.log("Begin loading resources");

		TextureLoader.loadTexture(GameResources.Textures.GRASS_BLOCK);
		MeshLoader.loadMesh(GameResources.Meshes.CUBE);
	}

	/**
	 * @return a game specific scene loader used to navigate the game
	 */
	@Override
	public SceneLoader[] getSceneLoaders() {
		return new SceneLoader[] { new TestSceneLoader() };
	}
}
