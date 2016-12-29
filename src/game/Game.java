package game;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.GameDisplay;
import engine.IGame;
import engine.game.objects.Camera;
import engine.game.objects.GameObject;
import engine.graphics.Renderer;
import engine.graphics.geo.Mesh;
import engine.input.InputHandler;
import engine.input.Key;
import engine.input.KeyboardInput;
import engine.utils.debug.Logger;

/**
 * Our game entry point
 * 
 * @author brandon.porter
 *
 */
public class Game implements IGame {
	private static final float CAMERA_POS_STEP = 0.3f;
	private static final float CAMERA_ROT_STEP = 5;

	private static final Logger _log = new Logger("Game", Logger.LoggerLevel.DEBUG);

	private final Renderer _renderer = new Renderer();
	private GameObject _gameObject;

	private Vector3f _cameraInc = new Vector3f();
	private Vector2f _cameraRot = new Vector2f();

	@Override
	public void init(GameDisplay display) throws Exception {
		_log.debug("Initializing game");
		// Register new camera to display
		display.registerCamera(new Camera());

		// Init renderer
		_renderer.init();

		float[] vertices = new float[] { 
				// VO
				-0.5f, 0.5f, 0.5f,
				// V1
				-0.5f, -0.5f, 0.5f,
				// V2
				0.5f, -0.5f, 0.5f,
				// V3
				0.5f, 0.5f, 0.5f,
				// V4
				-0.5f, 0.5f, -0.5f,
				// V5
				0.5f, 0.5f, -0.5f,
				// V6
				-0.5f, -0.5f, -0.5f,
				// V7
				0.5f, -0.5f, -0.5f 
		};

		int[] indices = new int[] {
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				4, 0, 3, 5, 4, 3,
				// Right face
				3, 2, 7, 5, 3, 7,
				// Left face
				0, 1, 6, 4, 0, 6,
				// Bottom face
				6, 1, 2, 7, 6, 2,
				// Back face
				4, 6, 7, 5, 4, 7
		};

		this._gameObject = new GameObject(new Mesh(vertices, null, null, indices));

		display.getCamera().getPosition().z = 3f;
		display.getCamera().updateViewMatrix();
	}

	@Override
	public void processInput(InputHandler handler) {
		KeyboardInput keyboard = handler.getKeyboard();
		_cameraInc.set(0, 0, 0);
		_cameraRot.set(0, 0);

		// Position updates
		if (keyboard.keyDown(Key.W))
			_cameraInc.z = -1;
		else if (keyboard.keyDown(Key.S))
			_cameraInc.z = 1;

		if (keyboard.keyDown(Key.A))
			_cameraInc.x = -1;
		else if (keyboard.keyDown(Key.D))
			_cameraInc.x = 1;

		if (keyboard.keyDown(Key.Z))
			_cameraInc.y = -1;
		else if (keyboard.keyDown(Key.X))
			_cameraInc.y = 1;

		// Rotation updates
		if (keyboard.keyDown(Key.LEFT))
			_cameraRot.y = -1;
		else if (keyboard.keyDown(Key.RIGHT))
			_cameraRot.y = 1;

		if (keyboard.keyDown(Key.UP))
			_cameraRot.x = -1;
		else if (keyboard.keyDown(Key.DOWN))
			_cameraRot.x = 1;

	}

	@Override
	public void update(GameDisplay display) {
		display.getCamera().movePosition(_cameraInc.x * CAMERA_POS_STEP, _cameraInc.y * CAMERA_POS_STEP,
				_cameraInc.z * CAMERA_POS_STEP);
		display.getCamera().moveRotation(_cameraRot.x * CAMERA_ROT_STEP, _cameraRot.y * CAMERA_ROT_STEP, 0);
		display.getCamera().updateViewMatrix();

		float rotation = _gameObject.getRotation().x + 1.5f;
		if (rotation > 360) {
			rotation = 0;
		}
		_gameObject.setRotation(rotation, rotation, rotation);
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
