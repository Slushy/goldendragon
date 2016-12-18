package com.gd.engine;

/**
 * The game window is what the user sees when interacting with the game
 * 
 * @author brandon.porter
 *
 */
public class GameWindow  {

	private String _title;
	private int _width;
	private int _height;

	/**
	 * Constructs a game window
	 * 
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 */
	public GameWindow(String title, int width, int height) {
		this._title = title;
		this._width = width;
		this._height = height;
	}
	
	/**
	 * Initializes the game window
	 */
	public void init() {
		// TODO: Implement me
	}
	
	/**
	 * Cleans up the window instance
	 */
	public void dispose() {
		// TODO: Implement me
	}
	
	/**
	 * Gets the window title
	 * @return text displayed on the game window
	 */
	public String getTitle() {
		return _title;
	}
	
	/**
	 * Sets the window title
	 * @param title text displayed on the game window
	 */
	public void setTitle(String title) {
		this._title = title;
	}
}
