package engine.graphics.geo;

import org.lwjgl.opengl.GL11;

/**
 * Represents our geometric vertices of a game object
 * 
 * @author brandon.porter
 *
 */
public class Mesh {
	private final VAO _vao;
	private final int _vertexCount;

	/**
	 * Construct a new mesh
	 * 
	 * @param vertexPositions
	 * @param indices
	 * @throws Exception
	 */
	public Mesh(float[] vertexPositions, float[] textureCoords, float[] normals, int[] indices) throws Exception {
		this._vertexCount = indices.length;

		// Create and bind the VAO
		this._vao = new VAO();
		_vao.use();

		// Bind all VBOS
		_vao.bindVBO(VBO.POSITION, vertexPositions);
		//_vao.bindVBO(VBO.TEXTURE, textureCoords);
		//_vao.bindVBO(VBO.NORMAL, normals);
		_vao.bindVBO(VBO.INDEX, indices);
		// Done binding
		_vao.done();
	}

	/**
	 * Renders our mesh object
	 */
	public void render() {
		_vao.use();

		// Draw game object
		GL11.glDrawElements(GL11.GL_TRIANGLES, _vertexCount, GL11.GL_UNSIGNED_INT, 0);

		_vao.done();
	}

	/**
	 * Disposes the mesh and any VAO/VBOS
	 */
	public void dispose() {
		_vao.dispose();
	}
}
