package engine.graphics.geometry;

import engine.common.Entity;

/**
 * Represents the geometric vertices for a game object
 * 
 * @author brandon.porter
 *
 */
public class Mesh extends Entity {
	private VAO _vao;
	private int _vertexCount = -1;

	/**
	 * Constructs a new mesh with the filename
	 * 
	 * @param fileName
	 *            the fileName (including extension) of the mesh
	 */
	public Mesh(String fileName) {
		super(fileName);
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
	 * @return the mesh file name
	 */
	public String getFileName() {
		return getName();
	}

	/**
	 * @return the number of vertices for this mesh
	 */
	public int getVertexCount() {
		return _vertexCount;
	}

	/**
	 * @return the vao for the mesh
	 */
	public VAO getVAO() {
		return _vao;
	}

	/**
	 * Sets the vbo data for this mesh, making it officially loaded and ready
	 * (if not null)
	 * 
	 * @param vao
	 *            the vertex array object containing all the loaded vbos
	 * @param vertexCount
	 *            the number of vertexes this mesh has
	 */
	public void setVAO(VAO vao, int vertexCount) {
		this._vao = vao;
		this._vertexCount = vertexCount;
	}

	/**
	 * Disposes VAO/VBOS and any other entities relating to this mesh
	 */
	@Override
	protected void onDispose() {
		_vao.dispose();
	}
}
