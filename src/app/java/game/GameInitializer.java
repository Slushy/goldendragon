package game;

import engine.IGameInitializer;
import engine.resources.loaders.MeshLoader;
import engine.resources.loaders.TextureLoader;
import engine.scenes.SceneLoader;
import engine.utils.Debug;
import game.scenes.loaders.TestSceneLoader;

/**
 * Defines how our game will initialize
 * 
 * @author Brandon Porter
 *
 */
public class GameInitializer implements IGameInitializer {
	@Override
	public SceneLoader getApplicationSplashLoader() {
		// return new SplashLoader();
		return null;
	}

	/**
	 * Called by the engine to load the game resources (in a separate thread)
	 * 
	 * @throws Exception
	 */
	@Override
	public void loadResources() throws Exception {
		Debug.log("Begin loading resources");

		TextureLoader.loadTexture(GameResources.Textures.GRASS_BLOCK);
		MeshLoader.loadMesh(GameResources.Meshes.CUBE);
		MeshLoader.loadMesh(GameResources.Meshes.BUNNY);
	}

	/**
	 * @return a game specific scene loader used to navigate the game
	 */
	@Override
	public SceneLoader[] getSceneLoaders() {
		return new SceneLoader[] { new TestSceneLoader() };
	}

	@Override
	public void dispose() {
		// TODO: resource disposing
	}
}
