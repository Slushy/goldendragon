package engine.graphics.geo;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.utils.debug.Logger;

/**
 * A VAO (Vertex Array Object) holds all of our VBO's (Vertex Buffer Object) for
 * each mesh. These "lists" of vbos are typically called attribute lists
 * 
 * @author brandon.porter
 *
 */
public class VAO implements IBindable {
	private static final Logger _log = new Logger("VAO");

	private final int _vaoId;
	private final List<VBO> _vbos = new ArrayList<>();

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
		_log.debug("use [%d]: attr count: %d", _vaoId, _attributeCount);
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
	public void bindVBO(VBO vbo, float[] data) throws Exception {
		vbo.bindData(data);
		storeVBO(vbo, GL11.GL_FLOAT);
	}

	/**
	 * Creates a vertex buffer object for this VAO
	 * 
	 * @param vboType
	 * @param data
	 * @throws Exception
	 */
	public void bindVBO(VBO vbo, int[] data) throws Exception {
		vbo.bindData(data);
		storeVBO(vbo, GL11.GL_INT);
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
		_log.debug("Disposing VAO");

		// Unbind and dispose any attached vbos
		int attrCount = 0;
		for (VBO vbo : _vbos) {
			// Unbind attribute
			if (vbo.isAttribute() && attrCount < _attributeCount)
				GL20.glDisableVertexAttribArray(attrCount++);

			// Delete the VBO
			vbo.dispose();
		}

		// clear any vbo data
		_vbos.clear();
		_attributeCount = 0;

		// Unbind and delete the VAO
		done();
		GL30.glDeleteVertexArrays(_vaoId);
	}

	/*
	 * Stores the vbo in memory and adds to attribute list if applicable
	 */
	private void storeVBO(VBO vbo, int attrType) {
		_log.debug("Store VBO: %s", vbo);
		_vbos.add(vbo);

		// Check and add vbo to attribute list
		if (vbo.isAttribute()) {
			GL20.glVertexAttribPointer(_attributeCount++, vbo.getAttrSize(), attrType, false, 0, 0);
			_log.debug("VBO added to list");
		}

		// Tell the VBO we are done with it
		vbo.done();
	}
}
