package game.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import engine.common.GameObject;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.resources.loaders.TextureLoader;
import engine.scenes.SceneLoader;

/**
 * Loads the test scene
 * 
 * @author Brandon Porter
 *
 */
public class TestSceneLoader extends SceneLoader {
	public static final String NAME = "Test Scene";
	/**
	 * Constructs a new Test Scene loader
	 */
	public TestSceneLoader() {
		super(NAME);
	}

	/**
	 * Loads the game objects for the Test Scene
	 */
	@Override
	protected List<GameObject> loadGameObjectsForScene() throws Exception {
		List<GameObject> gameObjects = new ArrayList<>();
	
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
		Mesh mesh = new Mesh(new Mesh.MeshVBOData(vertices, texCoords, indices));
		mesh.setMaterial(new Material(TextureLoader.loadTexture("grassblock.png")));
		
		// Create game object with mesh renderer
		GameObject cube = new GameObject("Cube");
		cube.addComponent(new MeshRenderer(mesh));
		cube.getTransform().setPosZ(-5);
		
//		for (int i = 0; i < 5000; i++) {
//			// Create game object with mesh renderer
//			GameObject cube2 = new GameObject("Cube");
//			cube2.addComponent(new MeshRenderer(mesh));
//			
//			int randomX = ThreadLocalRandom.current().nextInt(-20, 20);
//			int randomY = ThreadLocalRandom.current().nextInt(-20, 20);
//			int randomZ = ThreadLocalRandom.current().nextInt(-20, 20);
//			cube2.getTransform().setPosition(randomX, randomY, randomZ);
//			
//			gameObjects.add(cube2);
//		}
		
		
		GameObject script = new GameObject("Scene Behavior");
		script.addComponent(new TestSceneBehavior());
		
		gameObjects.add(cube);
		gameObjects.add(script);
		
		return gameObjects;
	}
}
