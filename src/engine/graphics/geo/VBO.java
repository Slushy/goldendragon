package engine.graphics.geo;

import org.lwjgl.opengl.GL15;

import engine.utils.Utils;

/**
 * A VBO (Vertex Buffer Object) is a memory buffer stored in the graphics card
 * memory that stores vertices
 * 
 * @author brandon.porter
 *
 */
public enum VBO {
	Position(GL15.GL_ARRAY_BUFFER, 3), Index(GL15.GL_ELEMENT_ARRAY_BUFFER, -1);

	private final int _bufferTarget;
	private final int _attrSize;

	private int _vboId = -1;

	/**
	 * Constructs a VBO object
	 * 
	 * @param bufferTarget
	 * @param isAttribute
	 */
	private VBO(int bufferTarget, int attrSize) {
		this._bufferTarget = bufferTarget;
		this._attrSize = attrSize;
	}

	/**
	 * Checks if the current vbo is an attribute in a VAO
	 * 
	 * @return
	 */
	public boolean isAttribute() {
		return _attrSize != -1;
	}

	/**
	 * @return current attribute size
	 */
	public int getAttrSize() {
		return _attrSize;
	}

	/**
	 * Creates a new vbo and binds the passed in data
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void bindData(float[] data) throws Exception {
		this._vboId = createVBO();
		GL15.glBufferData(_bufferTarget, Utils.loadBuffer(data), GL15.GL_STATIC_DRAW);
	}

	/**
	 * Creates a new vbo and binds the passed in data
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void bindData(int[] data) throws Exception {
		this._vboId = createVBO();
		GL15.glBufferData(_bufferTarget, Utils.loadBuffer(data), GL15.GL_STATIC_DRAW);
	}

	/**
	 * Tell OpenGL that we are done with this VBO
	 */
	public void done() {
		if (isAttribute())
			GL15.glBindBuffer(_bufferTarget, 0);
	}

	/**
	 * Unbind and deletes this VBO from graphics memory
	 */
	public void dispose() {
		if (_vboId != -1) {
			done(); // Just in case
			GL15.glDeleteBuffers(_vboId);
		}
	}

	@Override
	public String toString() {
		String name = super.toString();
		return String.format("%s [%d]: type: %d, size: %d", name, _vboId, _bufferTarget, _attrSize);
	}

	/*
	 * Creates a new VBO and binds it for use
	 */
	private int createVBO() throws Exception {
		if (_vboId != -1)
			throw new Exception("This VBO has already been created");

		int vboId = GL15.glGenBuffers();

		GL15.glBindBuffer(_bufferTarget, vboId);
		return vboId;
	}
}
