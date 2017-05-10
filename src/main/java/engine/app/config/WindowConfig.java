package engine.app.config;

/**
 * Configurable window settings at app launch
 * 
 * @author Brandon Porter
 *
 */
public class WindowConfig {
	
	/**
	 * Construct the config object within this package only
	 */
	protected WindowConfig() {	
	}
	
	/**
	 * Whether the window is resizable
	 */
	public boolean resizable = Defaults.Window.RESIZABLE;

	/**
	 * Whether to limit the GPU to output frames as high as the refresh rate
	 * of the monitor. If enabled it prevents screen tearing but can also
	 * introduce minor input lag.
	 */
	public boolean vSync = Defaults.Window.VSYNC;
	
	/**
	 * Whether or not to display the FPS (frames per second) on the window title bar
	 */
	public boolean showFPS = Defaults.Window.SHOW_FPS;
}
