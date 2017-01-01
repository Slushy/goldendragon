package game;

import engine.IGameInitializer;
import engine.resources.loaders.TextureLoader;
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

	/**
	 * Called by the engine to load the game resources (in a separate thread)
	 * @throws Exception 
	 */
	@Override
	public void loadResources() throws Exception {
		Debug.log("Begin loading resources");
		
		TextureLoader.loadTexture(GameResources.Textures.GRASS_BLOCK);
	}

	/**
	 * @return a game specific scene loader used to navigate the game
	 */
	@Override
	public SceneLoader[] getSceneLoaders() {
		return new SceneLoader[] { new TestSceneLoader() };
	}

}
