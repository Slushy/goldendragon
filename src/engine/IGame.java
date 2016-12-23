package engine;

/**
 * Defines the blueprint of every game logic entry point
 * 
 * @author brandon.porter
 *
 */
public interface IGame {
	void init();

	void processInput();

	void update();

	void render();

	void dispose();
}
