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

	public GameDisplay(String title, int width, int height) {
		this(title, width, height, new Window.WindowOptions());
	}
	
	public GameDisplay(String title, int width, int height, Window.WindowOptions windowOptions) {
		this(title, width, height, windowOptions, new GraphicsController.GraphicsOptions());
	}
	
	public GameDisplay(String title, int width, int height, GraphicsController.GraphicsOptions graphicsOptions) {
		this(title, width, height, new Window.WindowOptions(), graphicsOptions);
	}

	public GameDisplay(String title, int width, int height, Window.WindowOptions windowOptions,
			GraphicsController.GraphicsOptions graphicsOptions) {
		this._window = new OpenGLWindow(title, width, height, windowOptions);
		this._graphicsController = new OpenGLGraphicsController(graphicsOptions);
	}
}
