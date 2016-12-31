package game;

import engine.IGameInitializer;
import engine.scenes.SceneLoader;
import engine.utils.Logger;
import game.scenes.TestSceneLoader;

/**
 * Defines how our game will initialize
 * 
 * @author Brandon Porter
 *
 */
public class GameInitializer implements IGameInitializer {
	private static final Logger _log = new Logger("GameInitializer", Logger.LoggerLevel.DEBUG);

	/**
	 * Called by the engine to load the game resources (in a separate thread)
	 */
	@Override
	public void loadResources() {
		_log.debug("Begin loading resources");
	}

	/**
	 * @return a game specific scene loader used to navigate the game
	 */
	@Override
	public SceneLoader[] getSceneLoaders() {
		_log.debug("Retrieving game scene loader");
		return new SceneLoader[] { new TestSceneLoader() };
	}

}
