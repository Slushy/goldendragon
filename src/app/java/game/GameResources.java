package game;

import engine.graphics.geometry.Mesh;
import engine.graphics.geometry.Texture;
import engine.resources.loaders.MeshLoader;
import engine.resources.loaders.TextureLoader;

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

		// List of all textures for simple loading/destroying
		private static final Texture[] _texturesToLoad = new Texture[] { GRASS_BLOCK };

		// Static class
		private Textures() {
		}

		/**
		 * Loads all textures, should only be called at the beginning of the
		 * game
		 * 
		 * @throws Exception
		 */
		public static void loadAll() throws Exception {
			for (Texture texture : _texturesToLoad) {
				if (!texture.isLoaded())
					TextureLoader.loadTexture(texture);
			}
		}

		/**
		 * Disposes all textures, should only be called when the game closes
		 */
		public static void disposeAll() {
			for (Texture texture : _texturesToLoad) {
				if (texture.isLoaded())
					texture.dispose();
			}
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
		/**
		 * Stanford test bunny
		 */
		public static final Mesh BUNNY = new Mesh("bunny.obj");

		// List of all meshes for simple loading/destroying
		private static final Mesh[] _meshesToLoad = new Mesh[] { CUBE, BUNNY };

		// Static class
		private Meshes() {
		}

		/**
		 * Loads all meshes, should only be called at the beginning of the game
		 * 
		 * @throws Exception
		 */
		public static void loadAll() throws Exception {
			for (Mesh mesh : _meshesToLoad) {
				if (!mesh.isLoaded())
					MeshLoader.loadMesh(mesh);
			}
		}

		/**
		 * Disposes all meshes, should only be called when the game closes
		 */
		public static void disposeAll() {
			for (Mesh mesh : _meshesToLoad) {
				if (mesh.isLoaded())
					mesh.dispose();
			}
		}
	}
}
