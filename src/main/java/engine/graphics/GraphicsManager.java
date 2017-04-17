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
	 * INTERNAL USE ONLY
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		Debug.log("Initializing shader programs");
		for (ShaderType shaderType : ShaderType.values())
			shaderType.initialize();
	}

	/**
	 * Disposes each shader program
	 * 
	 * INTERNAL USE ONLY
	 */
	public static void dispose() {
		Debug.log("Disposing shader programs");
		for (ShaderType shaderType : ShaderType.values())
			shaderType.dispose();
	}
}
