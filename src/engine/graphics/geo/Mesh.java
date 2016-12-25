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

	public Mesh(float[] vertexPositions, int[] indices) throws Exception {
		this._vao = new VAO();
		this._vertexCount = indices.length;

		// Bind all VBOS
		_vao.use();
		_vao.bindVBO(VBO.Index, indices);
		_vao.bindVBO(VBO.Position, vertexPositions);
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
