package engine;

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
}
