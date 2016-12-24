package engine.game.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GameObject {
	private final int _vaoId;
	private final int _positionVBOId;
	private final int _indexVBOId;
	private final float[] _vertexPositions;
	private final int[] _indices;
	
	public GameObject(float[] vertexPositions, int[] indices) {
		this._vertexPositions = vertexPositions;
		this._indices = indices;
		
		// Create & bind VAO
		this._vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(_vaoId);

		// Position VBO
		this._positionVBOId = GL15.glGenBuffers();
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexPositions.length);
		verticesBuffer.put(vertexPositions).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, _positionVBOId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		// Index VBO
		this._indexVBOId = GL15.glGenBuffers();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indexVBOId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		
		// Unbind VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// Unbind VAO
		GL30.glBindVertexArray(0);
	}

	public void render() {
		GL30.glBindVertexArray(_vaoId); // Bind VAO
		GL20.glEnableVertexAttribArray(0); // Bind Position VBO

		// Draw game object
		GL11.glDrawElements(GL11.GL_TRIANGLES, this._indices.length, GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0); // Unbind Position VBO
		GL30.glBindVertexArray(0); // Unbind VAO
	}

	/**
	 * Disposes the game object and any VAO/VBOS
	 */
	public void dispose() {
		GL20.glDisableVertexAttribArray(0);

		// Delete vbos
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(_positionVBOId);
		GL15.glDeleteBuffers(_indexVBOId);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(_vaoId);
	}
}
