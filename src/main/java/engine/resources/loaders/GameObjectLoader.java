package engine.resources.loaders;

import engine.game.objects.GameObject;
import engine.graphics.geometry.Mesh;
import engine.resources.ResourceManager;

/**
 * Parses resource files of supported types and loads them into memory as new
 * engine objects
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
	 * Loads the specified file as a game object
	 * 
	 * @param fileName
	 *            file name of the game object
	 * @return new game object
	 * @throws Exception
	 */
	public static GameObject loadGameObject(String fileName) throws Exception {
		Mesh mesh = null;

		// Parses the file type so we can support multiple types
		switch (ResourceManager.getFileType(fileName)) {
		case OBJ:
			mesh = OBJLoader.loadMesh(fileName);
			break;
		default:
			throw new Exception(String.format("Trying to load an invalid file type: %s as a game object.", fileName));
		}

		return new GameObject(mesh);
	}
}
