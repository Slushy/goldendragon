package engine.scenes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.Display;
import engine.common.Camera;
import engine.graphics.GraphicsManager;
import engine.graphics.ShaderType;
import engine.graphics.StandardShaderProgram;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.lighting.Attenuation;
import engine.lighting.DirectionalLight;
import engine.lighting.Light;
import engine.lighting.PointLight;
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
	private final List<PointLight> _pointLights = new ArrayList<PointLight>();

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
		_pointLights.clear();
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
	 * Adds a point light to scene
	 * 
	 * @param light
	 *            a point light component
	 */
	public void addLightToScene(PointLight light) {
		_pointLights.add(light);
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
	 * Loads the relevant display values to the shader program and renders our
	 * scene
	 * 
	 * @param scene
	 *            the scene to render to (i.e. the current active scene)
	 */
	protected void render(Scene scene) {
		Camera camera = scene.getCamera();
		StandardShaderProgram shaderProgram = GraphicsManager.getShader(ShaderType.STANDARD);

		// Starts the rendering process
		// Clear the current frame before we render the next frame
		Display.MAIN.getGraphicsController().clearGraphics();
		shaderProgram.bind();

		// Sets the variables that will not change between render cycles here to
		// reduce the amount of opengl calls

		// Viewport projection matrix (Camera bounds, field of view, display
		// width/height)
		shaderProgram.setProjectionMatrix(camera.getProjectionMatrix());

		// Adds the scene lightings to the shader
		renderLighting(shaderProgram, camera.getViewMatrix());

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

	/*
	 * Adds all lighting components that we are using in the scene to the shader
	 * program
	 */
	private void renderLighting(StandardShaderProgram shaderProgram, Matrix4f viewMatrix) {
		// Set ambient light - base color/brightness of every fragment
		shaderProgram.setAmbientLight(Light.AMBIENT_LIGHT.getLight());

		// Directional light (i.e. the sun)
		if (_directionalLight != null && !_directionalLight.isDisposed()) {
			// Remember, we only care about a directional lights direction from
			// its rotation, not the position
			Vector3f dirLightRotation = _directionalLight.getGameObject().getTransform().getRotation();
			Vector3f dirLightDirection = _transformation.getDirectionalLightDirection(dirLightRotation, viewMatrix);
			shaderProgram.setDirectionalLight(_directionalLight.getColor(), dirLightDirection,
					_directionalLight.getBrightness());
		}

		// Point lights
		if (_pointLights.size() > 0) {
			// Only doing 1 point light right now
			PointLight pointLight = _pointLights.get(0);

			Vector3f viewSpacePosition = _transformation
					.buildWorldViewVector(pointLight.getGameObject().getTransform().getPosition(), viewMatrix, true);
			shaderProgram.setPointLight(pointLight.getColor(), viewSpacePosition, pointLight.getBrightness(), pointLight.getRange());
		}
		
		// Attenuation
		Attenuation att = PointLight.ATTENUATION;
		shaderProgram.setLightAttenuation(att.getConstant(), att.getQuadratic());
	}
}
