package engine.utils.loaders;

import engine.game.objects.GameObject;
import engine.graphics.geo.Mesh;
import engine.utils.ResourceManager;

/**
 * Parses game object files of supported types and loads them into memory as a new game object
 * 
 * @author brandon.porter
 *
 */
public class GameObjectLoader {

	/*
	 * Private to prevent instantiation
	 */
	private GameObjectLoader() {
	}

	/**
	 * Loads the specified file into a game object
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static GameObject load(String fileName) throws Exception {
		Mesh mesh = null;

		// Parses the file type so we can support multiple loads
		switch (ResourceManager.getFileType(fileName)) {
		case OBJ:
			mesh = OBJLoader.loadMesh(fileName);
			break;
		default:
			throw new Exception(String.format("Trying to load an invalid file type: %s as a game object", fileName));
		}

		return new GameObject(mesh);
	}
}
