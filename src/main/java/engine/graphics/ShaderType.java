package engine.graphics;

/**
 * Our different types of shaders
 * 
 * @author brandon.porter
 *
 */
public enum ShaderType {
	STANDARD(0, new ShaderUniforms.Standard());

	private final int _sort;
	private final ShaderUniforms _shaderUniforms;
	private final ShaderProgram _shaderProgram;

	/*
	 * Constructs a shader type
	 */
	private ShaderType(int sort, ShaderUniforms shaderUniforms) {
		this._sort = sort;
		this._shaderUniforms = shaderUniforms;
		this._shaderProgram = new ShaderProgram();
	}

	/**
	 * @return the 0-indexed sort value for the shader. Defines what order
	 *         multiple shaders should be rendered in.
	 */
	public int getSort() {
		return _sort;
	}

	/**
	 * @return the shader program to use for this shader type
	 */
	public ShaderProgram getShaderProgram() {
		return _shaderProgram;
	}

	/**
	 * @return all the uniforms this shader type supports
	 */
	protected UniformType[] getUniforms() {
		return _shaderUniforms.getUniforms();
	}

	/**
	 * Initializes the shader program for this shader type
	 * 
	 * @throws Exception
	 */
	protected void initialize() throws Exception {
		this._shaderProgram.initialize(this, _shaderUniforms.getUniforms());
	}

	/**
	 * Disposes the shader program for this shader type
	 */
	protected void dispose() {
		this._shaderProgram.dispose();
	}
}
