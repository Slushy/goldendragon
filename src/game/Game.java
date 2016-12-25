package game;

import engine.GameDisplay;
import engine.IGame;
import engine.game.objects.Camera;
import engine.game.objects.GameObject;
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
	private GameObject _gameObject;

	@Override
	public void init(GameDisplay display) throws Exception {
		_log.debug("Initializing game");
		// Register new camera to display
		display.registerCamera(new Camera());

		// Init renderer
		_renderer.init();

		float z = -5f;
		float[] vertices = new float[] { -0.5f, 0.5f, z, -0.5f, -0.5f, z, 0.5f, -0.5f, z, 0.5f, 0.5f, z };

		int[] indices = new int[] { 0, 1, 3, 3, 1, 2 };

		this._gameObject = new GameObject(new Mesh(vertices, indices));
		this._gameObject.getRotation().rotateZ((float) Math.toRadians(45));

		display.getCamera().getPosition().z = 3f;
		display.getCamera().updateViewMatrix();
	}

	@Override
	public void processInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameDisplay display) {
		display.getCamera().getPosition().z = -3f;
		display.getCamera().updateViewMatrix();
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
