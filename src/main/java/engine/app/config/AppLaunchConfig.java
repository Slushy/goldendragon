package engine.app.config;

import engine.system.Defaults;

/**
 * Defines starting configuration settings to initialize the app
 * 
 * @author Brandon Porter
 *
 */
public class AppLaunchConfig {

	/**
	 * The configurable window settings at launch
	 */
	public final WindowConfig window = new WindowConfig();

	/**
	 * The configurable graphics settings at launch
	 */
	public final GraphicsConfig graphics = new GraphicsConfig();

	/**
	 * The max frames per second to render the screen at
	 */
	public int maxFPS = Defaults.Engine.MAX_FPS;

	/**
	 * The max amount of times game state can be updated per second
	 */
	public int maxUPS = Defaults.Engine.MAX_UPS;
}
