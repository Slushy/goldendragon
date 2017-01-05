package game;

import engine.IGameInitializer;
import engine.lighting.Light;
import engine.scenes.ApplicationSplashLoader;
import engine.scenes.SceneLoader;
import engine.utils.Debug;
import game.scenes.loaders.SplashLoader;
import game.scenes.loaders.TestSceneLoader;

/**
 * Defines how our game will initialize
 * 
 * @author Brandon Porter
 *
 */
public class GameInitializer implements IGameInitializer {

	/**
	 * Constructor that is called before the engine is started
	 */
	public GameInitializer() {
		// Sets the ambient light at the beginning of the scene 
		// - we don't need to reset it every time
		Light.setAmbientLightColor(1, 1, 1);
		Light.setAmbientLightBrightness(2);
	}

	@Override
	public ApplicationSplashLoader getApplicationSplashLoader() {
		return new SplashLoader();
	}

	/**
	 * Called by the engine to load the game resources (in a separate thread)
	 * 
	 * @throws Exception
	 */
	@Override
	public void loadResources() throws Exception {
		Debug.log("Begin loading resources");

		GameResources.Textures.loadAll();
		GameResources.Meshes.loadAll();
	}

	/**
	 * @return a game specific scene loader used to navigate the game
	 */
	@Override
	public SceneLoader[] getSceneLoaders() {
		return new SceneLoader[] { new TestSceneLoader() };
	}

	/**
	 * Dispose all game-generated textures and meshes
	 */
	@Override
	public void dispose() {
		GameResources.Textures.disposeAll();
		GameResources.Meshes.disposeAll();
	}
}
