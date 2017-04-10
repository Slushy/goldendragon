package engine.resources.loaders;

import engine.graphics.geometry.Mesh;
import engine.resources.RequestManager;
import engine.resources.ResourceManager;
import engine.utils.Debug;

/**
 * Parses resource files of supported types and loads them into memory as new
 * mesh objects
 * 
 * @author brandon.porter
 *
 */
public class MeshLoader {

	/*
	 * Private to prevent instantiation
	 */
	private MeshLoader() {
	}

	/**
	 * Loads in the file from disk as a new mesh and registers its VAO
	 * 
	 * @param fileName
	 *            file name (with extension) of the mesh to load
	 * @return new mesh
	 * @throws Exception
	 */
	public static Mesh loadMesh(String fileName) throws Exception {
		Mesh mesh = new Mesh(fileName);
		loadMesh(mesh, fileName);
		return mesh;
	}

	/**
	 * Loads the existing mesh from disk and registers its VAO
	 * 
	 * @param mesh
	 *            existing mesh to load
	 * @param fileName
	 *            file name (with extension) of the mesh to load
	 * @throws Exception
	 */
	public static void loadMesh(Mesh mesh, String fileName) throws Exception {
		Mesh.MeshVBOData vboData;

		// If the mesh is already loaded we
		// just return
		if (mesh.isLoaded())
			return;

		// Parses the file type so we can support multiple types
		switch (ResourceManager.getFileType(fileName)) {
		case OBJ:
			vboData = OBJLoader.loadVBOData(fileName);
			break;
		default:
			throw new Exception(String.format("Trying to load an invalid file type: %s as a game object.", fileName));
		}

		// Should we throw exception here?
		if (vboData == null) {
			Debug.error("Could not load vbo data for mesh: " + fileName);
			return;
		}

		// This needs to be on main thread
		boolean wasImmediate = RequestManager.makeGLRequestImmediate(() -> {
			try {
				mesh.loadVAO(vboData);
			} catch (Exception e) {
				Debug.error("Error creating VAO for mesh: " + fileName);
				e.printStackTrace();
			}
		});

		Debug.log("GL request to register mesh (" + mesh.getName() + ") was immediate: " + wasImmediate);
	}

	/**
	 * Loads each mesh into one big mesh. This MUST BE CALLED FROM THE MAIN
	 * THREAD
	 * 
	 * @param meshVboDatas
	 * @return the combined mesh of all the meshes
	 */
	public static Mesh loadCombinedMesh(Mesh.MeshVBOData[] meshVboDatas) {
		Mesh combinedMesh = new Mesh();

		// Get the total count of vertices we will have for the combined mesh
		int posCt = 0, texCt = 0, normCt = 0, indCt = 0;
		for (Mesh.MeshVBOData vboData : meshVboDatas) {
			posCt += vboData.vertexPositions.length;
			texCt += vboData.textureCoords.length;
			normCt += vboData.vertexNormals.length;
			indCt += vboData.indices.length;
		}

		float[] positions = new float[posCt];
		float[] texCoords = new float[texCt];
		float[] norms = new float[normCt];
		int[] indices = new int[indCt];

		posCt = 0;
		texCt = 0;
		normCt = 0;
		indCt = 0;
		for (Mesh.MeshVBOData vboData : meshVboDatas) {
			// Copy positions
			for (float val : vboData.vertexPositions)
				positions[posCt++] = val;
			// Copy texture coords
			for (float val : vboData.textureCoords)
				texCoords[texCt++] = val;
			// Copy normals
			for (float val : vboData.vertexNormals)
				norms[normCt++] = val;
			// Copy indices
			for (int val : vboData.indices)
				indices[indCt++] = val;
		}

		// Loading the new mesh needs to be on main thread
		boolean wasImmediate = RequestManager.makeGLRequestImmediate(() -> {
			try {
				combinedMesh.loadVAO(new Mesh.MeshVBOData(positions, texCoords, norms, indices));
			} catch (Exception e) {
				Debug.error("Error creating VAO for combined mesh");
				e.printStackTrace();
			}
		});

		Debug.log("GL request to register a combined mesh was immediate: " + wasImmediate);

		return combinedMesh;
	}
}
