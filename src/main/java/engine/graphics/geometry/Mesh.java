package engine.graphics.geometry;

import engine.common.Entity;
import engine.utils.Debug;

/**
 * Represents the geometric vertices for a game object
 * 
 * @author brandon.porter
 *
 */
public class Mesh extends Entity {
	private static final String ENTITY_NAME = "Mesh";

	private VAO _vao;
	private MeshVBOData _vboData;
	private int _vertexCount = -1;
	private int _triangleCount = -1;

	public Mesh() {
		super(ENTITY_NAME);
	}

	/**
	 * Constructs a new mesh with the specified name
	 * 
	 * @param name
	 *            the name of the mesh
	 */
	public Mesh(String name) {
		super(name);
	}

	/**
	 * A mesh is loaded once all of its vbo data (positions, texture coords,
	 * vertices, etc.) have been loaded and stored within this mesh instance
	 * 
	 * @return true if mesh is loaded and ready, false otherwise
	 */
	public final boolean isLoaded() {
		return _vao != null;
	}

	/**
	 * @return the number of vertices for this mesh
	 */
	public int getVertexCount() {
		return _vertexCount;
	}

	/**
	 * @return the number of triangles as determined by the index list
	 */
	public int getTriangleCount() {
		return _triangleCount;
	}

	/**
	 * Registers the vbo data with opengl. [WARNING] - This MUST be called from
	 * the main thread.
	 * 
	 * @param vboData
	 *            the vbo data to set for the mesh
	 */
	public void loadVAO(MeshVBOData vboData) {
		this._vboData = vboData;
		this._vertexCount = vboData.indices.length;
		this._triangleCount = vboData.indices.length / 3;

		Debug.log("Loading new mesh with Triangles: " + _triangleCount + ", Vertices: " + _vertexCount);
		
		// Create and bind new VAO
		this._vao = new VAO();
		_vao.use();

		// Create all VBOS (Cannot change this order, if you do
		// you will have to edit the hardcoded VBO locations in
		// the actual shader files - maybe that should be changed?)
		VBO[] vbos = { VBO.POSITION, VBO.TEXTURE, VBO.NORMAL };
		float[][] vboDataArrays = { vboData.vertexPositions, vboData.textureCoords, vboData.vertexNormals };

		// Interleaved VBOs are much better performance-wise
		_vao.bindInterleavedVBO(vboData.vertexPositions.length / 3, vbos, vboDataArrays);
		_vao.bindVBO(VBO.INDEX, vboData.indices);

		// Unbind and return new vao
		_vao.done();
	}

	/**
	 * @return the vao for the mesh
	 */
	public VAO getVAO() {
		return _vao;
	}

	/**
	 * @return the vertices for this mesh
	 */
	public MeshVBOData getVBOData() {
		return _vboData;
	}

	/**
	 * Disposes VAO/VBOS and any other entities relating to this mesh
	 */
	@Override
	protected void onDispose() {
		_vao.dispose();
	}

	/**
	 * Container used to hold data for loading in the mesh
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class MeshVBOData {
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
