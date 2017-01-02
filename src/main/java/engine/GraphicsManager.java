package engine;

import engine.graphics.ShaderProgram;
import engine.graphics.ShaderType;
import engine.graphics.StandardShaderProgram;
import engine.utils.Debug;

/**
 * Manages state for shaders/graphics. Internal use only
 * 
 * @author Brandon Porter
 *
 */
public final class GraphicsManager {
	private static boolean _initialized = false;

	// Static class
	private GraphicsManager() {
	}

	/**
	 * Retrieves the singleton shader program
	 * 
	 * @param shaderType
	 *            type of shader to receive
	 * @return shader program
	 */
	public static ShaderProgram getShader(ShaderType shaderType) {
		switch (shaderType) {
		case STANDARD:
			return StandardShaderProgram.Instance;
		}

		Debug.error("Trying to retrieve unknown shader");
		return null;
	}

	/**
	 * Initializes all of our graphics classes dealt with in game (i.e. shaders,
	 * materials)
	 * 
	 * @throws Exception
	 */
	protected static void init() throws Exception {
		if (_initialized) {
			Debug.warn("Trying to initialize graphics manager when already initialized");
			return;
		}

		// Initialize shaders
		initShaders();
		_initialized = true;
	}

	/**
	 * @return true after graphics has been initialized
	 */
	protected static boolean ready() {
		return _initialized;
	}

	/**
	 * Initializes our shaders
	 * 
	 * @throws Exception
	 */
	private static void initShaders() throws Exception {
		StandardShaderProgram.init();
		Debug.log("Initialized shader program");
	}
}
