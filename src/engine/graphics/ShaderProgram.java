package engine.graphics;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL20;

import engine.utils.ResourceManager;
import engine.utils.debug.Logger;

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
	}

	/**
	 * Registers a uniform (i.e. global variable) for use within our shaders
	 * 
	 * @param uniform
	 *            the name of the uniform variable to register
	 * @throws Exception
	 */
	public void registerUniform(String uniform) throws Exception {
		// Try to find the variable name from within our shader code
		int uniformLocation = GL20.glGetUniformLocation(_programId, uniform);
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform: " + uniform);
		}
		// We found it, so add it to the list of uniforms
		_uniforms.put(uniform, uniformLocation);
		_log.debug("Registered uniform '%s' at location %d for program %d", uniform, uniformLocation, _programId);
	}

	/**
	 * Loads and compiles the vertex shader for this program
	 * @throws Exception
	 */
	public void registerVertexShader() throws Exception {
		this._vertShaderId = registerShader(_shaderType.toString().toLowerCase() + ".vert", GL20.GL_VERTEX_SHADER);
		_log.debug("Registered new vertex shader: %d", _vertShaderId);
	}

	/**
	 * Loads and compiles the fragment shader for this program
	 * @throws Exception
	 */
	public void registerFragmentShader() throws Exception {
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
}
