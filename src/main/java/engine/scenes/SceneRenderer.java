package engine.scenes;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import engine.Display;
import engine.common.gameObjects.Camera;
import engine.graphics.GraphicsManager;
import engine.graphics.ShaderType;
import engine.graphics.StandardShaderProgram;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.utils.math.Transformation;

/**
 * The scene renderer is the main renderer to render our scene game objects
 * 
 * @author brandon.porter
 *
 */
public class SceneRenderer {
	private static SceneRenderer _instance = null;

	/**
	 * @return current scenes renderer
	 */
	protected static SceneRenderer instance() {
		if (_instance == null)
			_instance = new SceneRenderer();
		return _instance;
	}

	private final Map<Long, LinkedList<Long>> _meshMaterials = new LinkedHashMap<>();
	private final Map<Long, LinkedList<MeshRenderer>> _materialRenderers = new LinkedHashMap<>();

	// Singleton class
	private SceneRenderer() {
	}

	/**
	 * Clears the stored objects for new scene
	 */
	public void reset() {
		_meshMaterials.clear();
		_materialRenderers.clear();
	}

	/**
	 * Adds renderer to scene
	 * 
	 * @param renderer
	 */
	public void submitRendererForRenderering(MeshRenderer renderer) {
		long meshId = renderer.getMesh().getInstanceId();
		long matId = renderer.getMaterial().getInstanceId();

		// Check if mesh exists
		LinkedList<Long> materials = _meshMaterials.get(meshId);
		if (materials == null) {
			materials = new LinkedList<>();
			_meshMaterials.put(meshId, materials);
		}

		// Check if material exists
		LinkedList<MeshRenderer> renderers = _materialRenderers.get(matId);
		if (renderers == null) {
			materials.add(matId);
			renderers = new LinkedList<>();
			_materialRenderers.put(matId, renderers);
		}

		// Add Renderer to list
		renderers.add(renderer);
	}

	/**
	 * Renders the meshes to screen
	 */
	protected void render(Scene scene) {
		StandardShaderProgram shaderProgram = GraphicsManager.getShader(ShaderType.STANDARD);
		
		// Starts the rendering process
		// Clear the current frame before we render the next frame
		Display.MAIN.getGraphicsController().clearGraphics();
		shaderProgram.bind();

		Camera camera = scene.getCamera();
		shaderProgram.setProjectionMatrix(camera.getProjectionMatrix());

		// For each similar mesh
		for (long meshId : _meshMaterials.keySet()) {
			// For each similar material
			for (long matId : _meshMaterials.get(meshId)) {
				// For each renderer with the shared mesh & material
				Material mat = _materialRenderers.get(matId).peekFirst().getMaterial();
				mat.renderStart(shaderProgram);
				for (MeshRenderer renderer : _materialRenderers.get(matId)) {
					// Set the transformation matrix
					shaderProgram.setWorldViewMatrix(Transformation
							.buildWorldViewMatrix(renderer.getGameObject().getTransform(), camera.getViewMatrix()));
					// Tell the renderer to render
					renderer.render();
				}
				mat.renderEnd();
			}
		}

		// Ends the rendering process
		shaderProgram.unbind();
	}

	/**
	 * Disposes the renderer
	 */
	protected void dispose() {
		reset();
	}
}
