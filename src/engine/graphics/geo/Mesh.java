package engine.graphics.geo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Represents our geometric vertices of a game object
 * 
 * @author brandon.porter
 *
 */
public class Mesh {
	private final VAO _vao;
	private final int _vertexCount;

	private Texture _texture = null;

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
	 * Sets the texture of the mesh
	 * 
	 * @param texture
	 *            texture to display on mesh, or null to clear
	 */
	public void setTexture(Texture texture) {
		if (hasTexture())
			_texture.dispose();

		this._texture = texture;
	}

	/**
	 * @return texture of the mesh
	 */
	public Texture getTexture() {
		return _texture;
	}

	/**
	 * @return true if the mesh has a texture, false otherwise
	 */
	public boolean hasTexture() {
		return _texture != null;
	}

	/**
	 * Renders our mesh object
	 */
	public void render() {
		_vao.use();

		// Activate any textures
		if (hasTexture()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			_texture.use();
		}

		// Draw game object
		GL11.glDrawElements(GL11.GL_TRIANGLES, _vertexCount, GL11.GL_UNSIGNED_INT, 0);

		// Deactivate any textures
		if (hasTexture()) {
			_texture.done();
		}
		
		_vao.done();
	}

	/**
	 * Disposes VAO/VBOS and any other entities relating to this mesh
	 */
	public void dispose() {
		if (hasTexture()) {
			_texture.dispose();
		}
		
		_vao.dispose();
	}
}
