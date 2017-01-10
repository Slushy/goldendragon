package engine.resources.loaders;

import engine.graphics.geometry.Mesh;
import engine.graphics.geometry.VAO;
import engine.graphics.geometry.VBO;
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
		loadMesh(mesh);
		return mesh;
	}

	/**
	 * Loads the existing mesh from disk and registers its VAO
	 * 
	 * @param mesh
	 *            existing mesh to load
	 * @throws Exception
	 */
	public static void loadMesh(Mesh mesh) throws Exception {
		MeshVBOData vboData;

		// If the mesh is already loaded we
		// just return
		if (mesh.isLoaded())
			return;

		// Parses the file type so we can support multiple types
		String fileName = mesh.getFileName();
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
				VAO vao = createNewVAO(vboData);
				mesh.setVAO(vao, vboData.indices.length);
			} catch (Exception e) {
				Debug.error("Error creating VAO for mesh: " + fileName);
				e.printStackTrace();
			}
		});

		Debug.log("GL request to register mesh (" + mesh.getName() + ") was immediate: " + wasImmediate);
	}

	/*
	 * Registers the vbo data with opengl. [WARNING] - This MUST be called from
	 * the main thread.
	 */
	private static VAO createNewVAO(MeshVBOData vboData) throws Exception {
		// Create and bind new VAO
		VAO vao = new VAO();
		vao.use();

		// Create all VBOS (Cannot change this order, if you do
		// you will have to edit the hardcoded VBO locations in
		// the actual shader files - maybe that should be changed?)
		vao.bindVBO(VBO.POSITION, vboData.vertexPositions);
		vao.bindVBO(VBO.TEXTURE, vboData.textureCoords);
		vao.bindVBO(VBO.NORMAL, vboData.vertexNormals);
		vao.bindVBO(VBO.INDEX, vboData.indices);

		// Unbind and return new vao
		vao.done();
		return vao;
	}

	/**
	 * Container used to hold data for loading in the new mesh
	 * 
	 * @author Brandon Porter
	 *
	 */
	protected static class MeshVBOData {
		public final float[] vertexPositions;
		public final float[] textureCoords;
		public final float[] vertexNormals;
		public final int[] indices;

		/**
		 * Constructs a new mesh vbo data wrapper
		 * 
		 * @param vertexPositions
		 * @param textureCoords
		 * @param vertexNormals
		 * @param indices
		 */
		public MeshVBOData(float[] vertexPositions, float[] textureCoords, float[] vertexNormals, int[] indices) {
			this.vertexPositions = vertexPositions;
			this.textureCoords = textureCoords;
			this.vertexNormals = vertexNormals;
			this.indices = indices;
		}
	}
}
