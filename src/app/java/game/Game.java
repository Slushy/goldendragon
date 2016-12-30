package game;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.IGame;
import engine.game.objects.Camera;
import engine.game.objects.GameObject;
import engine.graphics.GameDisplay;
import engine.graphics.components.Renderer;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.graphics.geometry.Texture;
import engine.input.InputHandler;
import engine.input.Key;
import engine.input.KeyboardInput;
import engine.resources.loaders.TextureLoader;
import engine.utils.Logger;

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
				// V0
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
				0.5f, -0.5f, -0.5f,

				// For text coords in top face
				// V8: V4 repeated
				-0.5f, 0.5f, -0.5f,
				// V9: V5 repeated
				0.5f, 0.5f, -0.5f,
				// V10: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V11: V3 repeated
				0.5f, 0.5f, 0.5f,

				// For text coords in right face
				// V12: V3 repeated
				0.5f, 0.5f, 0.5f,
				// V13: V2 repeated
				0.5f, -0.5f, 0.5f,

				// For text coords in left face
				// V14: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V15: V1 repeated
				-0.5f, -0.5f, 0.5f,

				// For text coords in bottom face
				// V16: V6 repeated
				-0.5f, -0.5f, -0.5f,
				// V17: V7 repeated
				0.5f, -0.5f, -0.5f,
				// V18: V1 repeated
				-0.5f, -0.5f, 0.5f,
				// V19: V2 repeated
				0.5f, -0.5f, 0.5f };

		int[] indices = new int[] {
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				8, 10, 11, 9, 8, 11,
				// Right face
				12, 13, 7, 5, 12, 7,
				// Left face
				14, 15, 6, 4, 14, 6,
				// Bottom face
				16, 18, 19, 17, 16, 19,
				// Back face
				4, 6, 7, 5, 4, 7 };

		float[] texCoords = new float[] { 0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f,

				0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f,

				// For text coords in top face
				0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 1.0f,

				// For text coords in right face
				0.0f, 0.0f, 0.0f, 0.5f,

				// For text coords in left face
				0.5f, 0.0f, 0.5f, 0.5f,

				// For text coords in bottom face
				0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 1.0f, 0.5f };

		Texture texture = TextureLoader.loadTexture("grassblock.png");

		this._gameObject = new GameObject(new Mesh(vertices, texCoords, indices));
		this._gameObject.getMesh().setMaterial(new Material(texture));

		display.getCamera().setPosZ(3f);
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
		display.getCamera().move(_cameraInc.x * CAMERA_POS_STEP, _cameraInc.y * CAMERA_POS_STEP,
				_cameraInc.z * CAMERA_POS_STEP);
		display.getCamera().rotate(_cameraRot.x * CAMERA_ROT_STEP, _cameraRot.y * CAMERA_ROT_STEP, 0);
		display.getCamera().updateViewMatrix();

		_gameObject.rotate(1.5f, 1.5f, 1.5f);
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
