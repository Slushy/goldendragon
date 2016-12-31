package engine.scenes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.GameDisplay;
import engine.common.Component;
import engine.common.GameObject;
import engine.common.gameObjects.Camera;
import engine.graphics.GraphicsController;
import engine.graphics.rendering.SceneRenderer;
import engine.input.InputHandler;
import engine.input.Key;
import engine.input.KeyboardInput;

/**
 * Each "screen" of the game will be a type of scene that holds onto all of the
 * game objects within this scene and will render/update them as the engine
 * demands
 * 
 * @author Brandon Porter
 *
 */
public class Scene {
	private static final float CAMERA_POS_STEP = 0.3f;
	private static final float CAMERA_ROT_STEP = 5;

	private final List<GameObject> _gameObjects = new ArrayList<>();
	private final SceneRenderer _sceneRenderer = new SceneRenderer();

	private final Camera _camera = new Camera();

	private Vector3f _cameraInc = new Vector3f();
	private Vector2f _cameraRot = new Vector2f();

	private GameObject _gameObject;

	public Scene() {
		// For now every scene will have a default camera
		this.addGameObject(_camera);
	}

	/**
	 * Initializes the scene
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		_sceneRenderer.init(this);
		// TODO: initialize game objects?

		for (GameObject gameObject : _gameObjects) {
			if (gameObject.getName().equals("Cube"))
				this._gameObject = gameObject;
		}
	}

	/**
	 * Updates the scene
	 */
	public void update(InputHandler input) {
		processInput(input);

		_camera.move(_cameraInc.x * CAMERA_POS_STEP, _cameraInc.y * CAMERA_POS_STEP, _cameraInc.z * CAMERA_POS_STEP);
		_camera.getTransform().rotate(_cameraRot.x * CAMERA_ROT_STEP, _cameraRot.y * CAMERA_ROT_STEP, 0);
		_camera.updateViewMatrix();

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

	/**
	 * Renders the scene
	 * 
	 * @param graphics
	 *            the graphics controller for the current display
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void render(GraphicsController graphics) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		_sceneRenderer.preRender(graphics);

		// TODO: Event dispatcher to render game objects
		// For now we will just do it manually
		for (GameObject gameObject : _gameObjects) {
			for (Component comp : gameObject.getComponents()) {
				// Check if the component is setup for rendering
				if ((comp.getCapabilities() & Component.RENDER) == Component.RENDER) {
					// Invoke the render method
					Method method = comp.getClass().getDeclaredMethod("render", _sceneRenderer.getClass());
					method.setAccessible(true);
					method.invoke(comp, _sceneRenderer);
				}
			}
		}

		_sceneRenderer.endRender(graphics);
	}

	/**
	 * Adds the game object to the scene
	 * 
	 * @param gameObject
	 */
	public void addGameObject(GameObject gameObject) {
		_gameObjects.add(gameObject);
	}

	/**
	 * @return the camera for the scene
	 */
	public Camera getCamera() {
		return _camera;
	}

	/**
	 * Disposes the scene
	 */
	public void dispose() {
		for (GameObject gameObject : _gameObjects) {
			gameObject.dispose();
		}
	}
}
