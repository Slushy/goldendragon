package engine.graphics;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import engine.resources.ResourceManager;
import engine.utils.Logger;

/**
 * Hooks up data from our renderers to our shader code. Every instance of
 * renderer should have their own implementation of a shader program
 * 
 * @author brandon.porter
 *
 */
public abstract class ShaderProgram {
	private static final Logger _log = new Logger("ShaderProgram");

	private final int _programId;
	private final ShaderType _shaderType;
	private final Map<String, Integer> _uniforms = new HashMap<>();

	private int _vertShaderId;
	private int _fragShaderId;

	/**
	 * Constructs a new shader program
	 * 
	 * @param shaderType
	 *            the type of shader for this program
	 * @throws Exception
	 */
	public ShaderProgram(ShaderType shaderType) throws Exception {
		this._shaderType = shaderType;
		// Creates a shader program with openGL
		this._programId = GL20.glCreateProgram();
		if (_programId == 0) {
			throw new Exception("Could not create shader program");
		}
		_log.debug("Created new shader program: %d", _programId);
		
		// Next we register the shaders and link the program
		// TODO: More information on what link does
		registerShaders();
		linkProgram();
		
		// Register any uniforms
		registerUniforms();
	}
	
	/**
	 * Binds this shader program to OpenGL as the active shader program
	 * for the current render/calculation cycle
	 */
	public void bind() {
		GL20.glUseProgram(_programId);
	}

	/**
	 * Unbinds this shader program from openGL to end the current render/calculation
	 * cycle with this as the active program
	 */
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * Cleans up the shader program
	 */
	public void dispose() {
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

	/**
	 * Links the program TODO: More information on what link does
	 * @throws Exception
	 */
	protected void linkProgram() throws Exception {
		GL20.glLinkProgram(_programId);
		if (GL20.glGetProgrami(_programId, GL20.GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking shader code: " + GL20.glGetProgramInfoLog(_programId, 1024));
		}

		// Keep in only while debugging, unnecessary for release
		GL20.glValidateProgram(_programId);
		if (GL20.glGetProgrami(_programId, GL20.GL_VALIDATE_STATUS) == 0) {
			_log.warn("Warning validing shader code: " + GL20.glGetProgramInfoLog(_programId, 1024));
		}
		
		_log.debug("Program linked");
	}
	
	/**
	 * Registers a uniform (i.e. global variable) for use within our shaders
	 * 
	 * @param uniform
	 *            the name of the uniform variable to register
	 * @throws Exception
	 */
	protected void registerUniform(String uniform) throws Exception {
		// Try to find the variable name from within our shader code
		int uniformLocation = GL20.glGetUniformLocation(_programId, uniform);
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform: " + uniform);
		}
		// We found it, so add it to the list of uniforms
		_uniforms.put(uniform, uniformLocation);
		_log.debug("Registered uniform '%s' at location %d for program %d", uniform, uniformLocation, _programId);
	}
	
	protected int getUniform(String uniform) {
		int location = _uniforms.get(uniform);
		if (!_uniforms.containsKey(uniform) || location < 0) {
			throw new RuntimeException("Uniform [" + uniform + "] has not been created");
		}
		return location;
	}
	
	/**
	 * Sets a vector uniform
	 * @param uniform
	 * @param value
	 */
	protected void setUniform(String uniform, Vector3f value) {
		GL20.glUniform3f(getUniform(uniform), value.x, value.y, value.z);
	}
	
	protected void setUniform(String uniform, boolean value) {
		GL20.glUniform1i(getUniform(uniform), value ? 1 : 0);
	}
	
	/**
	 * Sets a matrix uniform
	 * @param uniform
	 * @param value
	 */
	protected void setUniform(String uniform, Matrix4f value) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		GL20.glUniformMatrix4fv(getUniform(uniform), false, buffer);
	}
	
	/**
	 * Loads and compiles the vertex shader for this program
	 * @throws Exception
	 */
	protected void registerVertexShader() throws Exception {
		this._vertShaderId = registerShader(_shaderType.toString().toLowerCase() + ".vert", GL20.GL_VERTEX_SHADER);
		_log.debug("Registered new vertex shader: %d", _vertShaderId);
	}

	/**
	 * Loads and compiles the fragment shader for this program
	 * @throws Exception
	 */
	protected void registerFragmentShader() throws Exception {
		this._fragShaderId = registerShader(_shaderType.toString().toLowerCase() + ".frag", GL20.GL_FRAGMENT_SHADER);
		_log.debug("Registered new fragment shader: %d", _fragShaderId);
	}
	
	/**
	 * Loads and compiles a generic shader file and returns the new shaderID
	 * @param fileName
	 * @param glShaderType
	 * @return
	 * @throws Exception
	 */
	protected int registerShader(String fileName, int glShaderType) throws Exception {
		// Load the shader file into a String
		String shaderCode = ResourceManager.loadShaderFile(fileName);

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
	
	/**
	 * Register any shaders for this shader program
	 * @throws Exception 
	 */
	protected abstract void registerShaders() throws Exception;
	
	/**
	 * Register any uniforms for this shader program
	 */
	protected abstract void registerUniforms() throws Exception;
}
