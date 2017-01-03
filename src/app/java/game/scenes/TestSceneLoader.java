package game.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import engine.common.GameObject;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.scenes.SceneLoader;
import game.GameResources;

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
		
		// Create mesh and set texture material
		Mesh mesh = GameResources.Meshes.CUBE;
		Material mat = new Material(GameResources.Textures.GRASS_BLOCK);
		
		// Create game object with mesh renderer
		GameObject cube = new GameObject("Cube");
		cube.addComponent(new MeshRenderer(mesh, mat));
		cube.getTransform().setPosZ(-5);
		
		for (int i = 0; i < 5; i++) {
			// Create game object with mesh renderer
			GameObject cube2 = new GameObject("Cube");
			cube2.addComponent(new MeshRenderer(mesh, mat));
			
			int randomX = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomY = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomZ = ThreadLocalRandom.current().nextInt(-20, 20);
			cube2.getTransform().setPosition(randomX, randomY, randomZ);
			
			gameObjects.add(cube2);
		}
		
		GameObject script = new GameObject("Scene Behavior");
		script.addComponent(new TestSceneBehavior());
		
		gameObjects.add(cube);
		gameObjects.add(script);
		
		return gameObjects;
	}
}
