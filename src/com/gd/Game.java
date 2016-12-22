package com.gd;

import com.gd.engine.IGame;
import com.gd.engine.utils.debug.Logger;

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
	public void dispose() {
		_log.debug("Disposing game");
	}

}
