package com.gd;

import com.gd.engine.IGame;

/**
 * Our game entry point
 * @author brandon.porter
 *
 */
public class Game implements IGame {

	@Override
	public void init() {
		System.out.println("Initialize game");
	}

	@Override
	public void dispose() {
		System.out.println("Dispose game");
	}

}
