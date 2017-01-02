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
	
	public static StandardShaderProgram Instance = null;
	
	private static final String COLOR = "color";
	private static final String USE_TEXTURE = "useTexture";
	private static final String PROJECTION_MATRIX = "projectionMatrix";
	private static final String WORLD_VIEW_MATRIX = "worldViewMatrix";

	public static void init() throws Exception {
		Instance = new StandardShaderProgram();
	}
	/**
	 * Constructs a scene shader program
	 * 
	 * @throws Exception
	 */
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
	}

	/**
	 * Sets the color uniform for the object
	 * 
	 * @param color
	 */
	public void setColor(Vector3f color) {
		super.setUniform(COLOR, color);
		super.setUniform(USE_TEXTURE, true);
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
