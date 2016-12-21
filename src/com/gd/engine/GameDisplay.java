package com.gd.engine;

import com.gd.engine.graphics.GraphicsController;
import com.gd.engine.graphics.OpenGLGraphicsController;
import com.gd.engine.graphics.OpenGLWindow;
import com.gd.engine.graphics.Window;

/**
 * Our game display that represents a game window and graphics controller
 * @author brandon.porter
 *
 */
public class GameDisplay {
	private final Window _window;
	private final GraphicsController _graphicsController;
 
	/**
	 * Constructs a game display
	 * @param title
	 * @param width
	 * @param height
	 */
	public GameDisplay(String title, int width, int height) {
		this(title, width, height, new Window.WindowOptions());
	}
	
	/**
	 * Constructs a game display
	 * @param title
	 * @param width
	 * @param height
	 * @param windowOptions
	 */
	public GameDisplay(String title, int width, int height, Window.WindowOptions windowOptions) {
		this(title, width, height, windowOptions, new GraphicsController.GraphicsOptions());
	}
	
	/**
	 * Constructs a game display
	 * @param title
	 * @param width
	 * @param height
	 * @param graphicsOptions
	 */
	public GameDisplay(String title, int width, int height, GraphicsController.GraphicsOptions graphicsOptions) {
		this(title, width, height, new Window.WindowOptions(), graphicsOptions);
	}

	/**
	 * Constructs a game display
	 * @param title
	 * @param width
	 * @param height
	 * @param windowOptions
	 * @param graphicsOptions
	 */
	public GameDisplay(String title, int width, int height, Window.WindowOptions windowOptions,
			GraphicsController.GraphicsOptions graphicsOptions) {
		this._window = new OpenGLWindow(title, width, height, windowOptions);
		this._graphicsController = new OpenGLGraphicsController(graphicsOptions);
	}
	
	public void init() {
		this._window.init();
		this._window.show();
		
		this._graphicsController.init();
	}
	
	public void update() {
		this._window.update();
	}
	
	public void dispose() {
		_window.dispose();
	}
}
