package engine;

/**
 * Defines the blueprint of every game logic entry point
 * 
 * @author brandon.porter
 *
 */
public interface IGame {
	void init() throws Exception;

	void processInput();

	void update();

	void render(GameDisplay display);

	void dispose();
}
