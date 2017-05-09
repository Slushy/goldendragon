package engine.rendering.shader;

import org.lwjgl.opengl.GL20;

import engine.utils.Debug;
import engine.utils.ResourceUtils;

/**
 * Hooks up data from our renderers to our shader code. Every instance of
 * renderer should have their own implementation of a shader program
 * 
 * @author brandon.porter
 *
 */
public class ShaderProgram {
	private final UniformData _uniformData = new UniformData();
	
	private int _programId = -1;
	private int _vertShaderId = -1;
	private int _fragShaderId = -1;

	/**
	 * Constructs a new shader program
	 * 
	 */
	protected ShaderProgram() {
	}
	
	/**
	 * Binds this shader program to OpenGL as the active shader program for the
	 * current render/calculation cycle
	 */
	public void bind() {
		GL20.glUseProgram(_programId);
	}

	/**
	 * Unbinds this shader program from openGL to end the current
	 * render/calculation cycle with this as the active program
	 */
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * @return the uniform data holding all of the uniform values and locations
	 *         for this shader
	 */
	public UniformData getUniformData() {
		return _uniformData;
	}
	
	/**
	 * Initializes the shader program
	 * 
	 * @param shaderType
	 *            the type of shader for this program
	 * @param uniformTypes
	 *            the uniform types to register into the shader
	 * @throws Exception
	 *             throws exception if a program cannot be created with OpenGL
	 */
	protected void initialize(ShaderType shaderType, UniformType[] uniformTypes) throws Exception {
		if (_programId > 0) {
			throw new Exception("This shader program has already been initialized");
		}
		
		// 1.) Create a shader program with openGL
		this._programId = GL20.glCreateProgram();
		if (_programId == 0) {
			throw new Exception("Could not create shader program");
		}

		// 2.) Load the shader code and register the shaders
		String shaderTypeName = shaderType.toString().toLowerCase();
		registerVertexShader(shaderTypeName);
		registerFragmentShader(shaderTypeName);

		// 3.) Link the shader program
		linkProgram();

		// 4.) Register the uniforms that can be used with this shader (the
		// shader program must be linked and ready before calling this)
		registerUniforms(uniformTypes);
	}

	/**
	 * Cleans up the shader program
	 */
	protected void dispose() {
		// Program might be active, so lets unbind first
		unbind();

		if (_programId == 0)
			return;

		// Cleanup the shaders from the program
		if (_vertShaderId != 0)
			GL20.glDetachShader(_programId, _vertShaderId);
		if (_fragShaderId != 0)
			GL20.glDetachShader(_programId, _fragShaderId);

		// Delete the program from memory
		GL20.glDeleteProgram(_programId);
	}

	/*
	 * Links the program TODO: More information on what link does
	 */
	private void linkProgram() throws Exception {
		GL20.glLinkProgram(_programId);
		if (GL20.glGetProgrami(_programId, GL20.GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking shader code: " + GL20.glGetProgramInfoLog(_programId, 1024));
		}

		// Keep in only while debugging, unnecessary for release
		GL20.glValidateProgram(_programId);
		if (GL20.glGetProgrami(_programId, GL20.GL_VALIDATE_STATUS) == 0) {
			Debug.warn("Warning validing shader code: " + GL20.glGetProgramInfoLog(_programId, 1024));
		}
	}

	/*
	 * Loads and compiles the vertex shader for this program
	 */
	private void registerVertexShader(String shaderTypeName) throws Exception {
		this._vertShaderId = registerShader(shaderTypeName + ".vert", GL20.GL_VERTEX_SHADER);
	}

	/*
	 * Loads and compiles the fragment shader for this program
	 */
	private void registerFragmentShader(String shaderTypeName) throws Exception {
		this._fragShaderId = registerShader(shaderTypeName + ".frag", GL20.GL_FRAGMENT_SHADER);
	}

	/*
	 * Loads and compiles a generic shader file and returns the new shaderID
	 */
	private int registerShader(String fileName, int glShaderType) throws Exception {
		// Load the shader file into a String
		String shaderCode = ResourceUtils.loadShaderFile(fileName);

		// Register a new shader with OpenGL
		int shaderId = GL20.glCreateShader(glShaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Code: " + shaderCode);
		}
		// Load the shader code into the new registered shader
		GL20.glShaderSource(shaderId, shaderCode);
		// Compile the shader code
		GL20.glCompileShader(shaderId);

		// Verify shader compilation succeeded
		if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + GL20.glGetShaderInfoLog(shaderId, 1024));
		}

		// Attach the shader to this program and return the new shaderId
		GL20.glAttachShader(_programId, shaderId);
		return shaderId;
	}

	/*
	 * Registers a uniform (i.e. global variable) for use within our shaders
	 */
	private void registerUniform(String uniformName) throws Exception {
		_uniformData.register(_programId, uniformName);
	}

	/*
	 * Register all uniforms of this shader type for this shader program
	 */
	private void registerUniforms(UniformType[] uniformTypes) throws Exception {
		for (UniformType uniformType : uniformTypes) {
			String nameRef = uniformType.getName();

			// Checks for array
			if (uniformType.isArray()) {
				for (int i = 0; i < uniformType.getCount(); i++) {
					String arrNameRef = String.format(nameRef, i);
					this.registerUniform(arrNameRef);
				}
			}
			// Default single name uniform (also contains struct)
			else {
				this.registerUniform(nameRef);
			}
		}
	}
}
