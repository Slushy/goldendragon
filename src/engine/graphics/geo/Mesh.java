package engine.graphics.geo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.GraphicsController;
import engine.utils.debug.Logger;

/**
 * Represents our geometric vertices of a game object
 * 
 * @author brandon.porter
 *
 */
public class Mesh {
	private static final Logger _log = new Logger("Mesh", Logger.LoggerLevel.DEBUG);

	private final VAO _vao;
	private final int _vertexCount;
	private Material _material = new Material();

	/**
	 * Construct a new mesh
	 * 
	 * @param vertexPositions
	 * @param textureCoords
	 * @param indices
	 * @throws Exception
	 */
	public Mesh(float[] vertexPositions, float[] textureCoords, int[] indices) throws Exception {
		this._vertexCount = indices.length;

		// Create and bind the VAO
		this._vao = new VAO();
		_vao.use();

		// Bind all VBOS
		_vao.bindVBO(VBO.POSITION, vertexPositions);
		_vao.bindVBO(VBO.TEXTURE, textureCoords);
		_vao.bindVBO(VBO.INDEX, indices);

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
}
