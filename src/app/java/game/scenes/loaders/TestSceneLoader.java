package game.scenes.loaders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import engine.common.GameObject;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.lighting.DirectionalLight;
import engine.lighting.PointLight;
import engine.scenes.SceneLoader;
import game.GameResources;
import game.scenes.TestSceneBehavior;

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

		String cubeName = "Cube";
		// Create dynamic placed cubes
		for (int i = 0; i < 5; i++) {
			// Create game object with mesh renderer
			GameObject cube = new GameObject(cubeName);
			cube.addComponent(new MeshRenderer(mesh, mat));

			int randomX = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomY = ThreadLocalRandom.current().nextInt(-20, 20);
			int randomZ = ThreadLocalRandom.current().nextInt(-20, 20);
			cube.getTransform().setPosition(randomX, randomY, randomZ);

			gameObjects.add(cube);
		}

		// Create a bunny
		GameObject bunny = new GameObject("Bunny");
		bunny.addComponent(new MeshRenderer(GameResources.Meshes.BUNNY, Material.DEFAULT));
		bunny.getTransform().setPosZ(-10);
		gameObjects.add(bunny);

		// Create our scene script
		GameObject script = new GameObject("Scene Behavior");
		script.addComponent(new TestSceneBehavior());
		gameObjects.add(script);
		
		// Create a sun
		DirectionalLight light = new DirectionalLight();
		light.setBrightness(1);
		light.setColor(1, 1, 1);
		GameObject sun = new GameObject("Sun");
		sun.addComponent(light);
		gameObjects.add(sun);
		
		// Create a point light
		PointLight pointLight = new PointLight();
		GameObject fakeLamp = new GameObject("Fake Lamp");
		fakeLamp.getTransform().setPosZ(-8);
		fakeLamp.addComponent(pointLight);
		fakeLamp.addComponent(new MeshRenderer(mesh, mat));
		fakeLamp.getTransform().setScale(0.3f);
		gameObjects.add(fakeLamp);

		// return all game objects for our test scene
		return gameObjects;
	}
}
