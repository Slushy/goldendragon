package game.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.Input;
import engine.TimeManager;
import engine.common.Behavior;
import engine.common.Camera;
import engine.common.GameObject;
import engine.graphics.components.MeshRenderer;
import engine.utils.inputs.Key;
import game.scenes.loaders.TestSceneLoader;

/**
 * A behavior that controls the Test Scene script
 * 
 * @author Brandon Porter
 *
 */
public class TestScene extends Behavior {
	private static final float CAMERA_POS_STEP = 7f;
	private static final float CAMERA_ROT_STEP = 120;

	private Vector3f _cameraInc = new Vector3f();
	private Vector2f _cameraRot = new Vector2f();

	private List<GameObject> _gameObjects = new ArrayList<>();
	private GameObject _cube;
	private MeshRenderer _rend;

	/**
	 * Constructs a new behavior for the Test Scene
	 */
	public TestScene() {
		super(TestSceneLoader.NAME);
	}

	@SuppressWarnings("unused")
	private void init() {
		this._cube = this.getScene().findGameObject("bunny");
		this._rend = _cube.getComponentByType(MeshRenderer.class);
		_gameObjects.add(_cube);
	}

	@SuppressWarnings("unused")
	private void update() {
		processInput();

		Camera camera = this.getScene().getCamera();

		float deltaTime = TimeManager.getDeltaTime();
		camera.move(_cameraInc.x * CAMERA_POS_STEP * deltaTime, _cameraInc.y * CAMERA_POS_STEP * deltaTime,
				_cameraInc.z * CAMERA_POS_STEP * deltaTime);
		camera.getTransform().rotate(_cameraRot.x * CAMERA_ROT_STEP * deltaTime,
				_cameraRot.y * CAMERA_ROT_STEP * deltaTime, 0);
		camera.updateViewMatrix();

		for (GameObject obj : _gameObjects) {
			float rot = 90 * deltaTime;
			obj.getTransform().rotate(rot, rot, rot);
		}

		// Add new game object
		if (Input.keyDown(Key.SPACE)) {
			GameObject cube = new GameObject("Cube");
			cube.addComponent(new MeshRenderer(_rend.getMesh(), _rend.getMaterial()));

			int randomX = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomY = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomZ = ThreadLocalRandom.current().nextInt(-20, 20);
			cube.getTransform().setPosition(randomX, randomY, randomZ);

			getScene().addGameObject(cube);
			_gameObjects.add(cube);
		}
	}

	/**
	 * TODO: REMOVE, Temporary only processing input similar to game class
	 * 
	 * @param handler
	 */
	private void processInput() {
		_cameraInc.set(0, 0, 0);
		_cameraRot.set(0, 0);

		// Position updates
		if (Input.keyDown(Key.W))
			_cameraInc.z = -1;
		else if (Input.keyDown(Key.S))
			_cameraInc.z = 1;

		if (Input.keyDown(Key.A))
			_cameraInc.x = -1;
		else if (Input.keyDown(Key.D))
			_cameraInc.x = 1;

		if (Input.keyDown(Key.Z))
			_cameraInc.y = -1;
		else if (Input.keyDown(Key.X))
			_cameraInc.y = 1;

		// Rotation updates
		if (Input.keyDown(Key.LEFT))
			_cameraRot.y = -1;
		else if (Input.keyDown(Key.RIGHT))
			_cameraRot.y = 1;

		if (Input.keyDown(Key.UP))
			_cameraRot.x = -1;
		else if (Input.keyDown(Key.DOWN))
			_cameraRot.x = 1;
	}
}
