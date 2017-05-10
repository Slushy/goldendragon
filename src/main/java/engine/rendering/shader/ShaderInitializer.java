package engine.rendering.shader;

import engine.utils.Debug;

/**
 * Manages state for shaders/graphics. Internal use only
 * 
 * @author Brandon Porter
 *
 */
public final class ShaderInitializer {
	/**
	 * The total amount of shaders we can have being rendered per frame
	 */
	public final static int TOTAL_SHADERS = ShaderType.values().length;
	
	// Static class
	private ShaderInitializer() {
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