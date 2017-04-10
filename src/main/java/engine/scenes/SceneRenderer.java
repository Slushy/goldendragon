package engine.scenes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3fc;

import engine.Display;
import engine.common.Camera;
import engine.common.Defaults;
import engine.common.Transform;
import engine.graphics.Material;
import engine.graphics.MaterialPropertyBlock;
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

	@SuppressWarnings("unchecked")
	private final LinkedList<MeshRenderer>[] _renderersPerShader = (LinkedList<MeshRenderer>[]) new LinkedList<?>[ShaderType
			.values().length];

	private final Transformation _transformation = new Transformation();
	private final List<PointLight> _pointLights = new ArrayList<PointLight>();
	private DirectionalLight _directionalLight = null;

	// Singleton class
	private SceneRenderer() {
		for (int i = 0; i < _renderersPerShader.length; i++)
			_renderersPerShader[i] = new LinkedList<MeshRenderer>();
	}

	/**
	 * Clears the stored objects for new scene
	 */
	public void reset() {
		_pointLights.clear();
		for (LinkedList<MeshRenderer> rendererList : _renderersPerShader)
			rendererList.clear();
	}

	/**
	 * Adds renderer to scene
	 * 
	 * INTERNAL USE ONLY
	 * 
	 * @param renderer
	 *            the renderer to add to the rendering pipeline for this scene
	 */
	public void submitRendererForRenderering(MeshRenderer renderer) {
		Material newMat = renderer.getMaterial();
		MaterialPropertyBlock newProps = renderer.getProperties();

		// -1 because the sort is 1-indexed base
		LinkedList<MeshRenderer> shaderList = _renderersPerShader[newMat.getShaderType().getSort() - 1];

		// Find the place in the linked list that the renderer should be added
		// to
		int placementIdx = 0;
		boolean sameMaterial = false;
		for (MeshRenderer currRenderer : shaderList) {
			// Don't re-add the same renderer
			if (currRenderer == renderer)
				return;

			Material currMat = currRenderer.getMaterial();
			MaterialPropertyBlock currProps = currRenderer.getProperties();

			// If mats are equal then we next check properties
			if (currMat.compare(newMat)) {
				sameMaterial = true;
			}
			// If the materials are not equal, but the previous one was
			// then break because we found the placement
			else if (sameMaterial) {
				break;
			}

			// If the property block is the same, then we break ONLY IF they
			// have the same material
			if (currProps.compare(newProps) && sameMaterial)
				break;

			placementIdx++;
		}

		// Adds the renderer to the list in correct order
		shaderList.add(placementIdx, renderer);
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
		// Clear the current frame before we render the next frame
		Display.MAIN.getGraphicsController().clearGraphics();

		// Start the rendering loop
		for (LinkedList<MeshRenderer> rendererList : _renderersPerShader) {
			if (rendererList.size() == 0)
				continue;

			// Binds the next shader program for use
			ShaderProgram shaderProgram = rendererList.getFirst().getMaterial().getShaderType().getShaderProgram();
			shaderProgram.bind();

			// Sets the shader "global" variables i.e. projection matrix
			UniformData uniformData = shaderProgram.getUniformData();
			uniformData.set(UniformType.PROJECTION_MATRIX, camera.getProjectionMatrix());

			// Render the per-scene lighting like the sun & ambient light
			// Point lights will eventually not be in this list because we
			// have a limit on how many point lights we can render in a single
			// pass. So we will eventually limit/change this based on lighting
			// maps or game object position
			renderLighting(uniformData, camera.getViewMatrix());

			// Loop over each renderer
			for (MeshRenderer currRenderer : rendererList) {
				// Delegate the material and entity rendering to each renderer
				currRenderer.render(_transformation, camera, uniformData);
			}

			// Current shader is done
			shaderProgram.unbind();
		}
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
				uniformData.set(String.format(UniformType.POINT_LIGHT_COS_HALF_ANGLE.getName(), i),
						((SpotLight) pointLight).getCosHalfAngle());
				uniformData.set(String.format(UniformType.POINT_LIGHT_IS_SPOT.getName(), i), true);
			}
		}

		// Attenuation
		Attenuation att = PointLight.ATTENUATION;
		uniformData.set(UniformType.ATTENUATION_CONSTANT, att.getConstant());
		uniformData.set(UniformType.ATTENUATION_QUADRATIC, att.getQuadratic());
	}
}
