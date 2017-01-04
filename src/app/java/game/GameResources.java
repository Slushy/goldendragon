package game;

import engine.graphics.geometry.Mesh;
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

	/**
	 * Statis class to reference the game meshes
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static final class Meshes {

		/**
		 * The 6 sided cube
		 */
		public static final Mesh CUBE = new Mesh("cube.obj");
		public static final Mesh BUNNY = new Mesh("bunny.obj");

		// Static class
		private Meshes() {
		}
	}
}
