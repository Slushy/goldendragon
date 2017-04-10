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
import engine.lighting.Light;
import engine.utils.inputs.Key;
import game.scenes.loaders.TestSceneLoader;

/**
 * A behavior that controls the Test Scene script
 * 
 * @author Brandon Porter
 *
 */
public class TestSceneBehavior extends Behavior {
	private static final float CAMERA_POS_STEP = 10;
	private static final float CAMERA_ROT_STEP = 120;
	private static final float GAMEOBJECT_ROT_STEP = 90;
	private static final float SUN_ROT_STEP = 90;

	private Vector3f _cameraInc = new Vector3f();
	private Vector2f _cameraRot = new Vector2f();
	private Vector2f _sunRot = new Vector2f();

	private List<GameObject> _gameObjects = new ArrayList<>();
	private GameObject _cube;
	private MeshRenderer _rend;
	private GameObject _sun;

	/**
	 * Constructs a new behavior for the Test Scene
	 */
	public TestSceneBehavior() {
		super(TestSceneLoader.NAME);
	}

	/**
	 * Called once at the beginning of the scene
	 */
	@SuppressWarnings("unused")
	private void start() {
		// Sets the ambient light (base brightness of every pixel) at the
		// beginning of the scene
		//
		// This should be used on a per-scene basis as it is basically used to
		// control nighttime/daytime brightness
		Light.AMBIENT_LIGHT.setColor(1, 1, 1);
		Light.AMBIENT_LIGHT.setBrightness(0.1f);

		this._cube = this.getScene().findGameObject("cube");
		if (_cube != null) {
			_gameObjects.add(_cube);
			this._rend = _cube.getComponentByType(MeshRenderer.class);
		}
		
		this._sun = getScene().findGameObject("Sun");
	}

	/**
	 * Called once per frame to update the scene
	 */
	@SuppressWarnings("unused")
	private void update() {
		float deltaTime = TimeManager.getDeltaTime();

		// Lets us move around our test scene
		moveCamera(deltaTime);
		// Can manually position sun for testing
		rotateSun(deltaTime);

		// Game object updates
		updateGameObjects(deltaTime);
	}

	// Any updates to any gameobjects for testing purposes
	private void updateGameObjects(float deltaTime) {
		// Add new game object dynamically to our scene
		if (Input.keyDown(Key.SPACE) && _rend != null) {
			GameObject cube = new GameObject("Cube");
			cube.addComponent(new MeshRenderer(_rend.getMesh(), _rend.getMaterial()));

			int randomX = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomY = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomZ = ThreadLocalRandom.current().nextInt(-20, 20);
			cube.getTransform().setPosition(randomX, randomY, randomZ);

			getScene().addGameObject(cube);
			_gameObjects.add(cube);
		}

		// Rotates any new game objects
		float rotSpeed = GAMEOBJECT_ROT_STEP * deltaTime;
		for (GameObject obj : _gameObjects) {
			obj.getTransform().rotate(rotSpeed, rotSpeed, rotSpeed);
		}
	}

	// Test script that rotates our "sun" directional light on input
	private void rotateSun(float deltaTime) {
		float rotSpeed = SUN_ROT_STEP * deltaTime;
		_sunRot.set(0, 0);

		// X-axis
		if (Input.keyDown(Key.K)) {
			_sunRot.x = -rotSpeed;
		} else if (Input.keyDown(Key.L)) {
			_sunRot.x = rotSpeed;
		}
		// Y-axis
		if (Input.keyDown(Key.I)) {
			_sunRot.y = -rotSpeed;
		} else if (Input.keyDown(Key.O)) {
			_sunRot.y = rotSpeed;
		}

		// Rotates the sun
		_sun.getTransform().rotate(_sunRot.x, _sunRot.y, 0);
	}

	// Moves our camera throughout the scene on input
	private void moveCamera(float deltaTime) {
		Camera camera = this.getScene().getCamera();
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

		camera.move(_cameraInc.x * CAMERA_POS_STEP * deltaTime, _cameraInc.y * CAMERA_POS_STEP * deltaTime,
				_cameraInc.z * CAMERA_POS_STEP * deltaTime);
		camera.getTransform().rotate(_cameraRot.x * CAMERA_ROT_STEP * deltaTime,
				_cameraRot.y * CAMERA_ROT_STEP * deltaTime, 0);
		camera.updateViewMatrix();
	}
}
