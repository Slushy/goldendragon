package game.scenes.loaders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import engine.app.SceneLoader;
import engine.assets.Mesh;
import engine.rendering.MeshRenderer;
import engine.rendering.material.MaterialPropertyBlock;
import engine.rendering.material.MeshMaterial;
import engine.scene.GameObject;
import engine.scene.lighting.DirectionalLight;
import engine.scene.lighting.PointLight;
import engine.scene.lighting.SpotLight;
import engine.scene.performance.SceneOptimizer;
import engine.utils.Debug;
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

		GameObject cubes = new GameObject("Cubes");

		// Create mesh and set texture material
		Mesh mesh = GameResources.Meshes.CUBE;
		MeshMaterial mat = new MeshMaterial(GameResources.Textures.GRASS_BLOCK);

		String cubeName = "Cube";
		// Create dynamic placed cubes
		for (int i = 0; i < 100; i++) {
			// Create game object with mesh renderer
			GameObject cube = new GameObject(cubeName + i);
			MeshRenderer cubeRenderer = new MeshRenderer(mesh, mat);
			cube.addComponent(cubeRenderer);

			// Setting custom color properties per renderer
			if (i < 50) {
				cubeRenderer.getProperties().setColor(1, 0, 0);
			} else if (i < 75) {
				cubeRenderer.getProperties().setColor(0, 1, 0);
			}

			int randomX = ThreadLocalRandom.current().nextInt(-40, 40);
			int randomY = ThreadLocalRandom.current().nextInt(-40, 40);
			int randomZ = ThreadLocalRandom.current().nextInt(-40, 40);
			cube.getTransform().setPosition(randomX, randomY, randomZ);
			// cube.getTransform().rotate(45, 56, 2);
			cube.setParent(cubes);
		}

		// Create a bunny
		GameObject bunny = new GameObject("Bunny");
		MeshMaterial bunnyMaterial = new MeshMaterial(MeshMaterial.DEFAULT);
		MaterialPropertyBlock props = bunnyMaterial.getProperties();
		props.setSpecularColor(0, 0, 1);
		props.setShininess(1);
		bunny.addComponent(new MeshRenderer(GameResources.Meshes.BUNNY, bunnyMaterial));
		bunny.getTransform().setPosZ(-10);
		bunny.getTransform().setPosY(-0.5f);

		bunny.setParent(cubes);

		Debug.log("Cubes children before batch: " + cubes.getChildren().size());
		SceneOptimizer.batchChildren(cubes, false);
		Debug.log("Cubes children after batch: " + cubes.getChildren().size());

		gameObjects.add(cubes);

		// Create our scene script
		GameObject script = new GameObject("Scene Behavior");
		script.addComponent(new TestSceneBehavior());
		gameObjects.add(script);

		// Create a sun
		DirectionalLight light = new DirectionalLight();
		light.setBrightness(0.5f);
		light.setColor(1, 1, 1);
		GameObject sun = new GameObject("Sun");
		sun.addComponent(light);
		gameObjects.add(sun);

		// Create a spot light
		SpotLight spotLight = new SpotLight(60f, 50f);
		spotLight.setColor(1, 0, 0);
		GameObject fakeLamp = new GameObject("Fake Lamp");
		fakeLamp.getTransform().setPosZ(-6);
		fakeLamp.addComponent(spotLight);
		fakeLamp.addComponent(new MeshRenderer(mesh, mat));
		fakeLamp.getTransform().setScale(0.3f);
		gameObjects.add(fakeLamp);

		// Set the spotlight parent to be the bunny
		fakeLamp.setParent(bunny);

		// Create another point light
		PointLight pointLight2 = new PointLight();
		pointLight2.setColor(0, 1, 0);
		GameObject fakeLamp2 = new GameObject("Fake Lamp2");
		fakeLamp2.getTransform().setPosZ(-8);
		fakeLamp2.addComponent(pointLight2);
		fakeLamp2.addComponent(new MeshRenderer(mesh, mat));
		fakeLamp2.getTransform().setScale(0.3f);
		gameObjects.add(fakeLamp2);

		// return all game objects for our test scene
		return gameObjects;
	}
}
