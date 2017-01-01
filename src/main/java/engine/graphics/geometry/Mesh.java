package engine.graphics.geometry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.utils.Logger;

/**
 * Represents our geometric vertices of a game object
 * 
 * @author brandon.porter
 *
 */
public class Mesh {
	private static final Logger _log = new Logger("Mesh", Logger.LoggerLevel.DEBUG);

	private VAO _vao;
	private int _vertexCount;

	private Material _material = new Material();

	/**
	 * Construct a new mesh
	 * 
	 * @param vertexPositions
	 * @param textureCoords
	 * @param indices
	 * @throws Exception
	 */
	public Mesh(MeshVBOData vboData) throws Exception {
		// We'll do this from here for now
		init(vboData);
	}

	/**
	 * Initializes the Mesh (must be done on main thread)
	 * 
	 * @throws Exception
	 */
	public void init(MeshVBOData vboData) throws Exception {
		this._vertexCount = vboData.indices.length;

		// Create and bind the VAO
		this._vao = new VAO();
		_vao.use();

		// Bind all VBOS
		_vao.bindVBO(VBO.POSITION, vboData.vertexPositions);
		_vao.bindVBO(VBO.TEXTURE, vboData.textureCoords);
		_vao.bindVBO(VBO.INDEX, vboData.indices);

		// Done binding
		_vao.done();
	}

	/**
	 * First disposes any existing material before setting the new one
	 * 
	 * @param material
	 *            material to display on mesh, or null to clear
	 */
	public void setMaterial(Material material) {
		if (material == null) {
			_log.warn("Shouldn't be setting a mesh's material to null, using default material");
			material = new Material();
		}
		_material.dispose();
		this._material = material;
	}

	/**
	 * @return copy of the current material of the mesh
	 */
	public Material cloneMaterial() {
		return new Material(_material);
	}

	public Material getMaterial() {
		return _material;
	}
	
	/**
	 * Renders our mesh object
	 */
	public void render() {
		_vao.use();

		// set active material
		if (_material.hasTexture()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			_material.getTexture().use();
		}

		// Draw game object
		GL11.glDrawElements(GL11.GL_TRIANGLES, _vertexCount, GL11.GL_UNSIGNED_INT, 0);

		// Clear active material
		if (_material.hasTexture()) {
			_material.getTexture().done();
		}

		_vao.done();
	}

	/**
	 * Disposes VAO/VBOS and any other entities relating to this mesh
	 */
	public void dispose() {
		_material.dispose();
		_vao.dispose();
	}

	/**
	 * Used to hold mesh vertices data while creating a new mesh
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class MeshVBOData {
		public final float[] vertexPositions;
		public final float[] textureCoords;
		public final int[] indices;

		/**
		 * Constructs a new mesh vbo data wrapper
		 * 
		 * @param vertexPositions
		 * @param textureCoords
		 * @param indices
		 */
		public MeshVBOData(float[] vertexPositions, float[] textureCoords, int[] indices) {
			this.vertexPositions = vertexPositions;
			this.textureCoords = textureCoords;
			this.indices = indices;
		}
	}
}
