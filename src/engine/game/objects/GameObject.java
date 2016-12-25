package engine.game.objects;

import org.lwjgl.opengl.GL11;

import engine.graphics.geo.VAO;
import engine.graphics.geo.VBO;

public class GameObject {
	private final VAO _vao;
	private final int _vertexCount;
	
	public GameObject(float[] vertexPositions, int[] indices) throws Exception {
		this._vao = new VAO();
		this._vertexCount = indices.length;
		
		// Bind all VBOS
		_vao.use();
		_vao.bindVBO(VBO.Index, indices);
		_vao.bindVBO(VBO.Position, vertexPositions);
		// Done binding
		_vao.done();
	}

	public void render() {
		_vao.use();

		// Draw game object	
		GL11.glDrawElements(GL11.GL_TRIANGLES, _vertexCount, GL11.GL_UNSIGNED_INT, 0);

		_vao.done();
	}

	/**
	 * Disposes the game object and any VAO/VBOS
	 */
	public void dispose() {
		_vao.dispose();
	}
}
