package game;

import engine.graphics.geometry.Texture;

/**
 * Static class to reference game resources
 * 
 * @author Brandon Porter
 *
 */
public final class GameResources {

	// Static class
	private GameResources() {
	}

	/**
	 * Static class to reference the game textures
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static final class Textures {
		/**
		 * The cube grass block
		 */
		public static final Texture GRASS_BLOCK = new Texture("grassblock.png");

		// Static class
		private Textures() {
		}
	}
}
