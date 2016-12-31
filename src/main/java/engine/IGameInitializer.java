package engine;

import engine.scenes.Scene;
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
	 */
	void loadResources();

	/**
	 * @return all the scene-specific loaders for the game
	 */
	SceneLoader[] getSceneLoaders();

	/**
	 * @return the splash to show at application launch, or none to skip
	 */
	default Scene getApplicationSplash() {
		return null;
	}
}
