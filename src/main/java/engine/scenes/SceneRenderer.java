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
import engine.graphics.GraphicsManager;
import engine.graphics.Material;
import engine.graphics.MaterialPropertyBlock;
import engine.graphics.ShaderProgram;
import engine.graphics.UniformData;
import engine.graphics.UniformType;
import engine.graphics.components.MeshRenderer;
import engine.lighting.Attenuation;
import engine.lighting.DirectionalLight;
import engine.lighting.Light;
import engine.lighting.PointLight;
import engine.lighting.SpotLight;
import engine.utils.Debug;
import engine.utils.math.Transformation;

/**
 * The scene renderer is the main renderer to render our scene game objects
 * 
 * @author brandon.porter
 *
 */
class SceneRenderer {
	// For each shader (indexed by shader priority) we will have a list of
	// renderers
	@SuppressWarnings("unchecked")
	private final LinkedList<MeshRenderer>[] _renderersPerShader = (LinkedList<MeshRenderer>[]) new LinkedList<?>[GraphicsManager.TOTAL_SHADERS];
	private final Transformation _transformation = new Transformation();
	private final List<PointLight> _pointLights = new ArrayList<PointLight>();

	private DirectionalLight _directionalLight = null;

	// Singleton class
	protected SceneRenderer() {
		for (int i = 0; i < _renderersPerShader.length; i++) {
			_renderersPerShader[i] = new LinkedList<MeshRenderer>();
		}
	}

	/**
	 * Adds renderer to scene
	 * 
	 * @param renderer
	 *            the renderer to add to the rendering pipeline for the current
	 *            scene
	 */
	protected void addRendererToScene(MeshRenderer newRenderer) {
		Material newMat = newRenderer.getMaterial();
		MaterialPropertyBlock newProps = newRenderer.getProperties();

		// Get the list of renderers based on the current material's shader sort
		// priority as key
		LinkedList<MeshRenderer> similarRenderers = _renderersPerShader[newMat.getShaderType().getSort()];

		// Find the place in the linked list where the renderer should be added
		int placementIdx = 0;
		boolean hasSameMaterial = false;
		for (MeshRenderer existingRenderer : similarRenderers) {
			// Return early if the new renderer already exists
			if (existingRenderer == newRenderer)
				return;

			Material existingMat = existingRenderer.getMaterial();
			MaterialPropertyBlock existingProps = existingRenderer.getProperties();

			// If mats are equal then next compare renderer-specific overrides
			if (existingMat.compare(newMat)) {
				hasSameMaterial = true;

				// If the existing renderer material overrides equal the new
				// renderer material overrides, then we've found our best
				// placement in the rendering list
				if (existingProps.compare(newProps))
					break;
			}
			// If the materials are not equal, but the previous one was then
			// break because we want to keep like materials together even if the
			// renderer specific overrides do not match
			else if (hasSameMaterial)
				break;

			placementIdx++;
		}

		// Adds the renderer to the list at the most performant rendering
		// position
		similarRenderers.add(placementIdx, newRenderer);
	}

	/**
	 * Adds a point light, spotlight or directional light to scene
	 * 
	 * @param light
	 *            a light component of any light type
	 */
	protected void addLightToScene(Light light) {
		// Currently we only support 1 directional light (though this could
		// probably easily be changed to support multiple), but having 1 is
		// probably ok since.. we don't need more then 1 sun now do we?
		if (light instanceof DirectionalLight)
			this._directionalLight = (DirectionalLight) light;
		
		// Pointlight could also be a spotlight (We should probably change
		// casting to spotlight to be here too instead of per frame)
		else if (light instanceof PointLight)
			_pointLights.add((PointLight) light);
		else
			Debug.error("Light not supported");
	}

	/**
	 * Loads the relevant display values to the shader program and renders our
	 * scene
	 * 
	 * @param scene
	 *            the scene to render to (i.e. the current active scene)
	 */
	protected void render(Display display, Camera camera) {
		// Clear the current frame before we render the next frame
		display.getGraphicsController().clearGraphics();

		// Loops over every shader in shader sort priority order and renders the
		// list of renderers for each shader
		for (LinkedList<MeshRenderer> similarRenderers : _renderersPerShader) {
			if (similarRenderers.size() == 0)
				continue;

			// Binds the newly active shader by getting the shader from the
			// first renderer in the shader list
			ShaderProgram shaderProgram = similarRenderers.getFirst().getMaterial().getShaderType().getShaderProgram();
			shaderProgram.bind();

			// Sets the shader's "global" variables (uniforms that do not change
			// between all game objects)
			UniformData uniformData = shaderProgram.getUniformData();
			uniformData.set(UniformType.PROJECTION_MATRIX, camera.getProjectionMatrix());

			// Render the per-scene lighting like the sun & ambient light
			// Point lights will eventually not be in this list because we
			// have a limit on how many point lights we can render in a single
			// pass. So we will eventually limit/change this based on lighting
			// maps or game object position
			renderLighting(uniformData, camera.getViewMatrix());

			// Delegate the material and entity rendering to each renderer
			for (MeshRenderer currRenderer : similarRenderers)
				currRenderer.render(_transformation, camera, uniformData);

			// Current shader is done
			shaderProgram.unbind();
		}
	}

	/**
	 * Disposes the renderer
	 */
	protected void dispose() {
		_pointLights.clear();
		for (LinkedList<MeshRenderer> rendererList : _renderersPerShader)
			rendererList.clear();
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
		for (int i = 0; i < maxLights && i < _pointLights.size(); i++) {
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
