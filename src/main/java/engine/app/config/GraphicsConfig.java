package engine.app.config;

import engine.system.Defaults;

/**
 * Configurable graphics settings at app launch
 * 
 * @author Brandon Porter
 *
 */
public class GraphicsConfig {
	
	/**
	 * Construct the config object within this package only
	 */
	protected GraphicsConfig() {	
	}
	
	/**
	 * Whether to display every model's vertex without applied texturing
	 */
	public boolean polygonMode = Defaults.Graphics.POLYGON_MODE;
}
