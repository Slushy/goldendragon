package game.scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.common.GameObject;
import engine.common.components.Behavior;
import engine.common.gameObjects.Camera;
import engine.input.InputHandler;
import engine.input.Key;
import engine.input.KeyboardInput;
import engine.scenes.Scene;

/**
 * A behavior that controls the Test Scene script
 * 
 * @author Brandon Porter
 *
 */
public class TestSceneBehavior extends Behavior {
	private static final float CAMERA_POS_STEP = 0.3f;
	private static final float CAMERA_ROT_STEP = 5;

	private Vector3f _cameraInc = new Vector3f();
	private Vector2f _cameraRot = new Vector2f();

	private GameObject _gameObject;

	/**
	 * Constructs a new behavior for the Test Scene
	 */
	public TestSceneBehavior() {
		super(TestSceneLoader.NAME);
	}
	
	private void init() {
		this._gameObject = this.getScene().findGameObject("Cube");
	}

	private void update(InputHandler input) {
		processInput(input);
		
		Camera camera = this.getScene().getCamera();
		
		camera.move(_cameraInc.x * CAMERA_POS_STEP, _cameraInc.y * CAMERA_POS_STEP, _cameraInc.z * CAMERA_POS_STEP);
		camera.getTransform().rotate(_cameraRot.x * CAMERA_ROT_STEP, _cameraRot.y * CAMERA_ROT_STEP, 0);
		camera.updateViewMatrix();

		if (_gameObject != null) {
			_gameObject.getTransform().rotate(1.5f, 1.5f, 1.5f);
		}
	}

	/**
	 * TODO: REMOVE, Temporary only processing input similar to game class
	 * 
	 * @param handler
	 */
	private void processInput(InputHandler handler) {
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
}
