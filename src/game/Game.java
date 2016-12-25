package game;

import engine.GameDisplay;
import engine.IGame;
import engine.graphics.Renderer;
import engine.graphics.geo.Mesh;
import engine.utils.debug.Logger;

/**
 * Our game entry point
 * 
 * @author brandon.porter
 *
 */
public class Game implements IGame {
	private static final Logger _log = new Logger("Game");

	private final Renderer _renderer = new Renderer();
	
	private Mesh _gameObject;
	@Override
	public void init() throws Exception {
		_log.debug("Initializing game");
		_renderer.init();
		
		float[] vertices = new float[] {
			-0.5f, 0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.5f, 0.5f, 0.0f
		};
		
		int[] indices = new int[] {
				0, 1, 3, 3, 1, 2
		};
		
		this._gameObject = new Mesh(vertices, indices);
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
	public void render(GameDisplay display) {
		_renderer.render(display, _gameObject);
	}
	
	@Override
	public void dispose() {
		_log.debug("Disposing game");
		_gameObject.dispose();
		_renderer.dispose();
	}

}
