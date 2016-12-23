package game;

import engine.IGame;
import engine.utils.debug.Logger;

/**
 * Our game entry point
 * 
 * @author brandon.porter
 *
 */
public class Game implements IGame {
	private static final Logger _log = new Logger("Game");

	@Override
	public void init() {
		_log.debug("Initializing game");
	}

	@Override
	public void processInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void dispose() {
		_log.debug("Disposing game");
	}

}
