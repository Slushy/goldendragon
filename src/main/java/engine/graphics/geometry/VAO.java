package engine.graphics.geometry;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.utils.Debug;
import engine.utils.Utils;

/**
 * A VAO (Vertex Array Object) holds all of our VBO's (Vertex Buffer Object) for
 * each mesh. These "lists" of vbos are typically called attribute lists
 * 
 * @author brandon.porter
 *
 */
public class VAO implements IBindable {
	private final int _vaoId;
	private final List<Integer> _vbos = new ArrayList<>();

	private int _attributeCount = 0;

	/**
	 * Constructs a vertex array object
	 */
	public VAO() {
		this._vaoId = GL30.glGenVertexArrays();
	}

	/**
	 * Sets this as the active VAO for openGL
	 */
	@Override
	public void use() {
		// Bind the VAO
		GL30.glBindVertexArray(_vaoId);

		// Bind each attribute
		for (int i = 0; i < _attributeCount; i++) {
			GL20.glEnableVertexAttribArray(i);
		}
	}

	/**
	 * Creates a vertex buffer object for this VAO
	 * 
	 * @param vboType
	 * @param data
	 * @throws Exception
	 */
	public void bindVBO(VBO vbo, float[] data) {
		_vbos.add(vbo.bindData(data));
		storeVBO(vbo, GL11.GL_FLOAT);
	}

	/**
	 * Creates a vertex buffer object for this VAO
	 * 
	 * @param vboType
	 * @param data
	 * @throws Exception
	 */
	public void bindVBO(VBO vbo, int[] data) {
		_vbos.add(vbo.bindData(data));
		storeVBO(vbo, GL11.GL_INT);
	}

	/**
	 * Creates an interleaved vertex buffer object for this VAO. An interleaved
	 * VBO is different in that instead of having 3 vbos for Position, Normals
	 * and Textures arranged like this: (PPPP) (NNNN) (TTTT), we "interleave"
	 * them to only have 1 vbo arranged like this (PNTPNTPNTPNT)
	 * 
	 * @param vertexCount
	 * @param vbos
	 * @param data
	 * @throws Exception
	 */
	public void bindInterleavedVBO(int vertexCount, VBO[] vbos, float[][] data) {
		if (vbos.length != data.length) {
			Debug.error("Trying to interleave vbos and data of unequal length");
			return;
		}

		// Interleave the data
		float[] interleavedData = interleaveFloatData(vertexCount, data);
		_vbos.add(VBO.INTERLEAVED.bindData(interleavedData));
		
		// Compute the size of an element (holds one of each vbo) in bytes
		int totalSize = 0;
		for (int i = 0; i < vbos.length; i++)
			totalSize += vbos[i].getAttrSize();
		int vertexByteLength = totalSize * Utils.FLOAT_SIZE_BYTES;
		
		// Store the vbo as an attribute
		for (int i = 0, currOffset = 0; i < vbos.length; i++) {
			storeVBO(vbos[i], GL11.GL_FLOAT, vertexByteLength, (currOffset * Utils.FLOAT_SIZE_BYTES), (i+1 == vbos.length));
			currOffset += vbos[i].getAttrSize();
		}
	}

	/**
	 * Tell OpenGL that we are done rendering this VAO
	 */
	@Override
	public void done() {
		// Unbind each attribute
		for (int i = 0; i < _attributeCount; i++) {
			GL20.glDisableVertexAttribArray(i);
		}

		// Unbind the VAO
		GL30.glBindVertexArray(0);
	}

	/**
	 * Disposes the VAO and any attached VBOS
	 */
	@Override
	public void dispose() {
		// Unbind and dispose any attached vbos
		int attrCount = 0;
		for (int vbo : _vbos) {
			// Unbind attribute
			if (attrCount < _attributeCount)
				GL20.glDisableVertexAttribArray(attrCount++);

			// Delete the VBO
			GL15.glDeleteBuffers(vbo);
		}

		// clear any vbo data
		_vbos.clear();
		_attributeCount = 0;

		// Unbind and delete the VAO
		done();
		GL30.glDeleteVertexArrays(_vaoId);
	}

	/**
	 * This method takes an array of float arrays and interleaves them into 1
	 * float array, e.g. 3 arrays for Position, Normals and Textures arranged
	 * like this: (PPPP) (NNNN) (TTTT), we "interleave" them to only have 1
	 * float array arranged like this (PNTPNTPNTPNT)
	 * 
	 * @param vertexCount
	 *            the number of vertices or indices in this mesh
	 * @param data
	 *            the array of float arrays
	 * @return the interleaved float array
	 */
	public static float[] interleaveFloatData(int vertexCount, float[][] data) {
		// First compute the total size of the interleaved buffer
		int bufferSize = 0;
		for (int i = 0; i < data.length; i++)
			bufferSize += data[i].length;
		float[] interleavedBuffer = new float[bufferSize];

		// Now loop over each vertex, vbo and element and store in that buffer
		// as interleaved e.g. [VBO1, VBO2, VBO3, VBO1, VBO2, VBO3...]

		// For each vertex
		int currPointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			// For each VBO
			for (int j = 0; j < data.length; j++) {
				// For each element
				int vboTypeLength = data[j].length / vertexCount;
				for (int k = 0; k < vboTypeLength; k++) {
					interleavedBuffer[currPointer++] = data[j][i * vboTypeLength + k];
				}
			}
		}

		return interleavedBuffer;
	}

	/*
	 * Stores the vbo in memory and adds to attribute list if applicable
	 */
	private void storeVBO(VBO vbo, int attrType) {
		storeVBO(vbo, attrType, 0, 0, true);
	}

	/*
	 * Stores the vbo in memory and adds to attribute list if applicable
	 */
	private void storeVBO(VBO vbo, int attrType, int stride, long position, boolean finished) {
		// Check and add vbo to attribute list
		if (vbo.isAttribute()) {
			GL20.glVertexAttribPointer(_attributeCount++, vbo.getAttrSize(), attrType, false, stride, position);
		}
		
		if (finished)
			// Tell the VBO we are done with it
			vbo.done();
	}
}
