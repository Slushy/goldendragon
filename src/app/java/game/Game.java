package game;

import java.lang.reflect.InvocationTargetException;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.GameDisplay;
import engine.IGame;
import engine.common.GameObject;
import engine.common.components.CameraProjection;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.input.InputHandler;
import engine.resources.loaders.TextureLoader;
import engine.scenes.Scene;
import engine.utils.Logger;

/**
 * Our game entry point
 * 
 * @author brandon.porter
 *
 */
public class Game implements IGame {
	private static final Logger _log = new Logger("Game", Logger.LoggerLevel.DEBUG);
	private Scene _scene = new Scene();

	@Override
	public void init(GameDisplay display) throws Exception {
		_log.debug("Initializing game");
		// Register new camera to display

		CameraProjection proj = _scene.getCamera().getComponentByType(CameraProjection.class);
		_log.debug("Camera Projection class: %s", proj);

		display.registerCamera(proj);
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

		// Create mesh and set texture material
		Mesh mesh = new Mesh(vertices, texCoords, indices);
		mesh.setMaterial(new Material(TextureLoader.loadTexture("grassblock.png")));

		// Create game object with mesh renderer
		GameObject cube = new GameObject("Cube");
		cube.addComponent(new MeshRenderer(mesh));
		cube.getTransform().setPosZ(-5);

		// Add that game object to scene
		_scene.addGameObject(cube);

		// Init renderer
		_scene.init();
	}

	@Override
	public void processInput(InputHandler handler) {
		// TODO: Remove
	}

	@Override
	public void update(GameDisplay display) {
		_scene.update(display.getInputHandler());
	}

	@Override
	public void render(GameDisplay display) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		_scene.render(display.getGraphicsController());
	}

	@Override
	public void dispose() {
		_log.debug("Disposing game");
		_scene.dispose();
	}

}
