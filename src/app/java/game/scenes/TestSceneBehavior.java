package game.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.common.GameObject;
import engine.common.components.Behavior;
import engine.common.gameObjects.Camera;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Mesh;
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

	private List<GameObject> _gameObjects = new ArrayList<>();
	private Mesh _mesh;
	
	private boolean _addNewObject = false;

	/**
	 * Constructs a new behavior for the Test Scene
	 */
	public TestSceneBehavior() {
		super(TestSceneLoader.NAME);
	}
	
	@Override
	public void init() {
		GameObject cube = this.getScene().findGameObject("Cube");
		_gameObjects.add(cube);
		
		this._mesh = cube.getComponentByType(MeshRenderer.class).getMesh();
	}

	@Override
	public void update(InputHandler input) {
		processInput(input);
		
		Camera camera = this.getScene().getCamera();
		
		camera.move(_cameraInc.x * CAMERA_POS_STEP, _cameraInc.y * CAMERA_POS_STEP, _cameraInc.z * CAMERA_POS_STEP);
		camera.getTransform().rotate(_cameraRot.x * CAMERA_ROT_STEP, _cameraRot.y * CAMERA_ROT_STEP, 0);
		camera.updateViewMatrix();

		for (GameObject obj : _gameObjects) {
			obj.getTransform().rotate(1.5f, 1.5f, 1.5f);
		}
		
		// Add new game object
		if (input.getKeyboard().keyDown(Key.SPACE)) {
			GameObject cube = new GameObject("Cube");
			cube.addComponent(new MeshRenderer(_mesh));
			
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
