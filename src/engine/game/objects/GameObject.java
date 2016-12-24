package engine.game.objects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GameObject {
	private final int _vaoId;
	private final int _vboId;
	private final float[] _vertexPositions;

	public GameObject(float[] vertexPositions) {
		this._vertexPositions = vertexPositions;
		
		// Create & bind VAO
		this._vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(_vaoId);

		// Position VBO
		this._vboId = GL15.glGenBuffers();
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexPositions.length);
		verticesBuffer.put(vertexPositions).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, _vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		// Unbind VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// Unbind VAO
		GL30.glBindVertexArray(0);
	}
	
	public void render() {
		GL30.glBindVertexArray(_vaoId); // Bind VAO
		GL20.glEnableVertexAttribArray(0); // Bind Position VBO
		
		// Draw game object
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, _vertexPositions.length / 3);
	
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
		GL15.glDeleteBuffers(_vboId);
		
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(_vaoId);
	}
}
