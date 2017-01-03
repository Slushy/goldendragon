package engine.graphics;

import engine.utils.Debug;

/**
 * Manages state for shaders/graphics. Internal use only
 * 
 * @author Brandon Porter
 *
 */
public final class GraphicsManager {

	// Static class
	private GraphicsManager() {
	}

	/**
	 * Initializes all of our graphics classes dealt with in game (i.e. shaders,
	 * materials)
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		// Initialize shaders
		StandardShaderProgram.init();
	}

	/**
	 * Retrieves the singleton shader program
	 * 
	 * @param shaderType
	 *            type of shader to receive
	 * @return shader program
	 */
	public static StandardShaderProgram getShader(ShaderType shaderType) {
		switch (shaderType) {
		case STANDARD:
			return StandardShaderProgram.getInstance();
		}

		Debug.error("Trying to retrieve unknown shader");
		return null;
	}

	/**
	 * Disposes each shader program
	 */
	public static void dispose() {
		for (ShaderType shaderType : ShaderType.values())
			getShader(shaderType).dispose();
	}
}
