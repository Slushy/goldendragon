package engine.scenes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3fc;

import engine.Display;
import engine.common.Camera;
import engine.common.Defaults;
import engine.common.Transform;
import engine.graphics.Material;
import engine.graphics.ShaderProgram;
import engine.graphics.ShaderType;
import engine.graphics.UniformData;
import engine.graphics.UniformType;
import engine.graphics.components.MeshRenderer;
import engine.lighting.Attenuation;
import engine.lighting.DirectionalLight;
import engine.lighting.Light;
import engine.lighting.PointLight;
import engine.lighting.SpotLight;
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
	 * Adds a point light or spotlight to scene
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
		ShaderProgram shaderProgram = ShaderType.STANDARD.getShaderProgram();

		UniformData uniformData = shaderProgram.getUniformData();

		// Starts the rendering process
		// Clear the current frame before we render the next frame
		Display.MAIN.getGraphicsController().clearGraphics();
		shaderProgram.bind();

		// Sets the variables that will not change between render cycles here to
		// reduce the amount of opengl calls

		// Viewport projection matrix (Camera bounds, field of view, display
		// width/height)
		uniformData.set(UniformType.PROJECTION_MATRIX, camera.getProjectionMatrix());
		// Adds the scene lightings to the shader
		renderLighting(uniformData, camera.getViewMatrix());

		// For each similar mesh
		for (long meshId : _meshMaterials.keySet()) {
			// For each similar material
			for (long matId : _meshMaterials.get(meshId)) {
				// For each renderer with the shared mesh & material
				Material mat = _materialRenderers.get(matId).peekFirst().getMaterial();
				mat.renderStart(shaderProgram);
				// Specular/shininess component
				uniformData.set(UniformType.SHININESS, mat.getShininess());
				uniformData.set(UniformType.SPECULAR_COLOR, mat.getSpecularColor());
				
				for (MeshRenderer renderer : _materialRenderers.get(matId)) {
					// Set the transformation matrix
					uniformData.set(UniformType.WORLD_VIEW_MATRIX, _transformation
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
	private void renderLighting(UniformData uniformData, Matrix4f viewMatrix) {
		// Set ambient light - base color/brightness of every fragment
		uniformData.set(UniformType.AMBIENT_LIGHT, Light.AMBIENT_LIGHT.getLight());

		// Directional light (i.e. the sun)
		if (_directionalLight != null && !_directionalLight.isDisposed()) {
			// Remember, we only care about a directional lights direction from
			// its rotation, not the position
			Vector3fc dirLightRotation = _directionalLight.getGameObject().getTransform().getRotation();
			Vector3fc dirLightDirection = _transformation.getFacingDirection(dirLightRotation, viewMatrix);

			uniformData.set(UniformType.DIRECTIONAL_LIGHT_COLOR, _directionalLight.getColor());
			uniformData.set(UniformType.DIRECTIONAL_LIGHT_DIRECTION, dirLightDirection);
			uniformData.set(UniformType.DIRECTIONAL_LIGHT_INTENSITY, _directionalLight.getBrightness());
		}

		// TODO: This will have to change per game object eventually
		// Right now we just display point lights and then if we have room do
		// spot lights.
		// Eventually we will get the closest light sources per game object.

		// Render each point light (or up until the max allowed point lights)
		int maxLights = Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT;
		int i = 0;
		for (i = 0; i < maxLights && i < _pointLights.size(); i++) {
			PointLight pointLight = _pointLights.get(i);
			Transform transform = pointLight.getGameObject().getTransform();

			Vector3fc viewSpacePosition = _transformation.buildWorldViewVector(transform.getPosition(), viewMatrix,
					true);

			uniformData.set(String.format(UniformType.POINT_LIGHT_COLOR.getName(), i), pointLight.getColor());
			uniformData.set(String.format(UniformType.POINT_LIGHT_INTENSITY.getName(), i), pointLight.getBrightness());
			uniformData.set(String.format(UniformType.POINT_LIGHT_POSITION.getName(), i), viewSpacePosition);
			uniformData.set(String.format(UniformType.POINT_LIGHT_RANGE.getName(), i), pointLight.getRange());
			
			// If it's a spotlight, register spotlight specific uniforms
			if (pointLight instanceof SpotLight) {
				Vector3fc facingDirection = _transformation.getFacingDirection(transform.getRotation(), viewMatrix);
				uniformData.set(String.format(UniformType.POINT_LIGHT_DIRECTION.getName(), i), facingDirection);
				uniformData.set(String.format(UniformType.POINT_LIGHT_COS_HALF_ANGLE.getName(), i), ((SpotLight)pointLight).getCosHalfAngle());
				uniformData.set(String.format(UniformType.POINT_LIGHT_IS_SPOT.getName(), i), true);
			}
		}
		
		// Attenuation
		Attenuation att = PointLight.ATTENUATION;
		uniformData.set(UniformType.ATTENUATION_CONSTANT, att.getConstant());
		uniformData.set(UniformType.ATTENUATION_QUADRATIC, att.getQuadratic());
	}
}
