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

		float z = -5f;
		float[] vertices = new float[] { -0.5f, 0.5f, z, -0.5f, -0.5f, z, 0.5f, -0.5f, z, 0.5f, 0.5f, z };

		int[] indices = new int[] { 0, 1, 3, 3, 1, 2 };

		this._gameObject = new GameObject(new Mesh(vertices, indices));
		//this._gameObject.getRotation().rotateZ((float) Math.toRadians(45));
		
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
		
		this._gameObject.getRotation().z += 1;
		//this._gameObject.getRotation().rotateZ((float) Math.toRadians(1));
		//this._gameObject.getRotation().y += (-0.02f);
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
