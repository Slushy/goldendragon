package engine;

import engine.input.InputHandler;

/**
 * Defines the blueprint of every game logic entry point
 * 
 * @author brandon.porter
 *
 */
public interface IGame {
	void init(GameDisplay display) throws Exception;

	void processInput(InputHandler inputHandler);

	void update(GameDisplay display);

	void render(GameDisplay display);

	void dispose();
}
