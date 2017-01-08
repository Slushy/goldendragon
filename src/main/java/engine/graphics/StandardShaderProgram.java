package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A Scene shader program to hold all of the scene uniforms and shader data
 * 
 * @author brandon.porter
 *
 */
public class StandardShaderProgram extends ShaderProgram {
	private static StandardShaderProgram _instance = null;

	/**
	 * @return instance for the standard shader program
	 */
	protected static StandardShaderProgram getInstance() {
		return _instance;
	}

	/**
	 * Initializes the standard shader program if it hasn't been
	 * 
	 * @return instance for the standard shader program
	 * @throws Exception
	 */
	protected static StandardShaderProgram init() throws Exception {
		if (_instance == null)
			_instance = new StandardShaderProgram();
		return _instance;
	}

	private static final String COLOR = "color";
	private static final String USE_TEXTURE = "useTexture";
	private static final String PROJECTION_MATRIX = "projectionMatrix";
	private static final String WORLD_VIEW_MATRIX = "worldViewMatrix";
	private static final String AMBIENT_LIGHT = "ambientLight";
	private static final String HAS_DIRECTIONAL_LIGHT = "hasDirectionalLight";
	private static final String DIRECTIONAL_LIGHT = "directionalLight";

	// Singleton shader
	private StandardShaderProgram() throws Exception {
		super(ShaderType.STANDARD);
	}

	@Override
	protected void registerShaders() throws Exception {
		super.registerVertexShader();
		super.registerFragmentShader();
	}

	@Override
	protected void registerUniforms() throws Exception {
		super.registerUniform(COLOR);
		super.registerUniform(USE_TEXTURE);
		super.registerUniform(PROJECTION_MATRIX);
		super.registerUniform(WORLD_VIEW_MATRIX);
		super.registerUniform(AMBIENT_LIGHT);
		super.registerUniform(HAS_DIRECTIONAL_LIGHT);
		super.registerUniform(DIRECTIONAL_LIGHT + ".color");
		super.registerUniform(DIRECTIONAL_LIGHT + ".direction");
		super.registerUniform(DIRECTIONAL_LIGHT + ".intensity");
	}

	/**
	 * Sets the color uniform for the object
	 * 
	 * @param color
	 */
	public void setColor(Vector3f color) {
		super.setUniform(COLOR, color);
	}

	/**
	 * Sets the directional light
	 * 
	 * @param color
	 * @param direction
	 * @param intensity
	 */
	public void setDirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		super.setUniform(DIRECTIONAL_LIGHT + ".color", color);
		super.setUniform(DIRECTIONAL_LIGHT + ".direction", direction);
		super.setUniform(DIRECTIONAL_LIGHT + ".intensity", intensity);
	}

	/**
	 * Sets the shader to use directional light calculations
	 * 
	 * @param hasDirectionaLight
	 */
	public void setHasDirectionalLight(boolean hasDirectionaLight) {
		super.setUniform(HAS_DIRECTIONAL_LIGHT, hasDirectionaLight);
	}

	/**
	 * Sets the ambient light of the scene
	 * 
	 * @param ambientLight
	 */
	public void setAmbientLight(Vector3f ambientLight) {
		super.setUniform(AMBIENT_LIGHT, ambientLight);
	}

	/**
	 * Sets whether or not this object should use a texture to render
	 * 
	 * @param useTexture
	 */
	public void useTexture(boolean useTexture) {
		super.setUniform(USE_TEXTURE, useTexture);
	}

	/**
	 * Sets the projection matrix uniform for the object
	 * 
	 * @param projection
	 */
	public void setProjectionMatrix(Matrix4f projection) {
		super.setUniform(PROJECTION_MATRIX, projection);
	}

	/**
	 * Sets the world view matrix uniform for the object
	 * 
	 * @param worldView
	 */
	public void setWorldViewMatrix(Matrix4f worldView) {
		super.setUniform(WORLD_VIEW_MATRIX, worldView);
	}
}
