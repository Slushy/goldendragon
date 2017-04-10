package engine.graphics.geometry;

import org.lwjgl.opengl.GL15;

import static engine.utils.math.VectorUtils.VECTOR_3D_SIZE;
import static engine.utils.math.VectorUtils.VECTOR_2D_SIZE;

import engine.utils.Utils;

/**
 * A VBO (Vertex Buffer Object) is a memory buffer stored in the graphics card
 * memory that stores vertices
 * 
 * @author brandon.porter
 *
 */
public enum VBO {
	POSITION(GL15.GL_ARRAY_BUFFER, VECTOR_3D_SIZE), TEXTURE(GL15.GL_ARRAY_BUFFER, VECTOR_2D_SIZE), NORMAL(
			GL15.GL_ARRAY_BUFFER,
			VECTOR_3D_SIZE), INDEX(GL15.GL_ELEMENT_ARRAY_BUFFER, -1), INTERLEAVED(GL15.GL_ARRAY_BUFFER, 0); // Interleaved is TBD at run time

	private final int _bufferTarget;
	private final int _attrSize;

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
		return _attrSize > -1;
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
	public int bindData(float[] data) {
		int vboId = createVBO(_bufferTarget);
		GL15.glBufferData(_bufferTarget, Utils.loadBuffer(data), GL15.GL_STATIC_DRAW);
		return vboId;
	}

	/**
	 * Creates a new vbo and binds the passed in data
	 * 
	 * @param data
	 * @throws Exception
	 */
	public int bindData(int[] data) {
		int vboId = createVBO(_bufferTarget);
		GL15.glBufferData(_bufferTarget, Utils.loadBuffer(data), GL15.GL_STATIC_DRAW);
		return vboId;
	}

	/**
	 * Tell OpenGL that we are done with this VBO
	 */
	public void done() {
		if (isAttribute())
			GL15.glBindBuffer(_bufferTarget, 0);
	}

	@Override
	public String toString() {
		String name = super.toString();
		return String.format("%s: type: %d, size: %d", name, _bufferTarget, _attrSize);
	}

	/*
	 * Creates a new VBO and binds it for use
	 */
	private static int createVBO(int bufferTarget) {
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(bufferTarget, vboId);
		return vboId;
	}
}
