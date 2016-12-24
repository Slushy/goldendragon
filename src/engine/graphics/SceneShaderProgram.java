package engine.graphics;

import engine.utils.debug.Logger;

/**
 * A Scene shader program to hold all of the scene uniforms and shader data
 * 
 * @author brandon.porter
 *
 */
public class SceneShaderProgram extends ShaderProgram {
	private static final Logger _log = new Logger("SceneShaderProgram");
	
	public static final String COLOR = "color";
	
	/**
	 * Constructs a scene shader program
	 * 
	 * @throws Exception
	 */
	public SceneShaderProgram() throws Exception {
		super(ShaderType.Scene);
	}

	@Override
	protected void registerShaders() throws Exception {
		super.registerVertexShader();
		super.registerFragmentShader();
	}

	@Override
	protected void registerUniforms() throws Exception {
		super.registerUniform(COLOR);
	}

}
