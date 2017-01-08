package engine.scenes;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.joml.Vector3f;

import engine.Display;
import engine.common.Camera;
import engine.graphics.GraphicsManager;
import engine.graphics.ShaderType;
import engine.graphics.StandardShaderProgram;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.lighting.DirectionalLight;
import engine.lighting.Light;
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

	private final Transformation _transformation = new Transformation();
	private final Map<Long, LinkedList<Long>> _meshMaterials = new LinkedHashMap<>();
	private final Map<Long, LinkedList<MeshRenderer>> _materialRenderers = new LinkedHashMap<>();
	private DirectionalLight _directionalLight = null;
	
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
	 * Adds a directional light to scene. If two are added the last one to be
	 * added becomes the current directional light. Currently we will only
	 * support one directional light - why? do we have more than 1 sun?
	 * 
	 * @param dirLight
	 *            the directional light component to be added
	 */
	public void setDirectionalLight(DirectionalLight dirLight) {
		this._directionalLight = dirLight;
	}

	/**
	 * Renders the meshes to screen
	 */
	protected void render(Scene scene) {
		Camera camera = scene.getCamera();
		StandardShaderProgram shaderProgram = GraphicsManager.getShader(ShaderType.STANDARD);

		// Starts the rendering process
		// Clear the current frame before we render the next frame
		Display.MAIN.getGraphicsController().clearGraphics();
		shaderProgram.bind();

		// Sets the variables that will not change between render cycles
		
		// Viewport projection matrix (Camera bounds, field of view, display width/height)
		shaderProgram.setProjectionMatrix(camera.getProjectionMatrix());
		
		// Lighting 
		shaderProgram.setAmbientLight(Light.AMBIENT_LIGHT.getLight());
		if (_directionalLight != null) {
			// Remember, we only care about a directional lights direction from
			// its rotation, not the position
			Vector3f dirLightRotation = _directionalLight.getGameObject().getTransform().getRotation();
			Vector3f dirLightDirection = _transformation.getDirectionalLightDirection(dirLightRotation,
					camera.getViewMatrix());
			shaderProgram.setDirectionalLight(_directionalLight.getColor(), dirLightDirection,
					_directionalLight.getBrightness());
		}

		// For each similar mesh
		for (long meshId : _meshMaterials.keySet()) {
			// For each similar material
			for (long matId : _meshMaterials.get(meshId)) {
				// For each renderer with the shared mesh & material
				Material mat = _materialRenderers.get(matId).peekFirst().getMaterial();
				mat.renderStart(shaderProgram);
				for (MeshRenderer renderer : _materialRenderers.get(matId)) {
					// Set the transformation matrix
					shaderProgram.setWorldViewMatrix(_transformation
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
