package engine;

import engine.scenes.ApplicationSplashLoader;
import engine.scenes.SceneLoader;

/**
 * Defines the blueprint of how a game initializes
 * 
 * @author brandon.porter
 *
 */
public interface IGameInitializer {
	/**
	 * Called by the engine to load the game resources (in a separate thread)
	 * 
	 * @throws Exception
	 */
	void loadResources() throws Exception;

	/**
	 * @return all the scene-specific loaders for the game
	 */
	SceneLoader[] getSceneLoaders();

	/**
	 * @return the splash to show at application launch, or none to skip
	 */
	default ApplicationSplashLoader getApplicationSplashLoader() {
		return null;
	}

	/**
	 * Called when the game is closing; this should unload all resources created
	 */
	void dispose();
}
