package engine.graphics;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import engine.graphics.geometry.Texture;
import engine.utils.math.MatrixUtils;

public class UniformData {
	private static final int NULL_TEXTURE = -1;
	private final HashMap<String, Uniform> _uniformLocations = new HashMap<>();
	private final FloatBuffer _fb = BufferUtils.createFloatBuffer(MatrixUtils.MATRIX_4D_SIZE);

	// Max amount of textures, currently it's only 1 (main texture)
	private final int[] _textureIds = new int[1];

	/**
	 * Constructs a UniformData instance
	 */
	protected UniformData() {
		for (int i = 0; i < _textureIds.length; i++)
			_textureIds[i] = NULL_TEXTURE;
	}

	/**
	 * Checks if the shader can use this uniform
	 * 
	 * @param uniformName
	 *            the name of the uniform to check
	 * @return true if the shader can use the uniform
	 */
	public boolean hasUniform(String uniformName) {
		return _uniformLocations.containsKey(uniformName);
	}

	/**
	 * Sets a 3D vector uniform
	 * 
	 * @param uniformName
	 *            name of the uniform
	 * @param value
	 *            value of the 3D vector
	 */
	public void set(String uniformName, Vector3fc value) {
		Uniform uniform = getUniform(uniformName);
		GL20.glUniform3f(uniform.location, value.x(), value.y(), value.z());
		// Keep the value cached
		uniform.value = value;
	}

	/**
	 * Sets a 3D vector uniform
	 * 
	 * @param uniformType
	 *            type of the uniform
	 * @param value
	 *            value of the 3D vector
	 */
	public void set(UniformType uniformType, Vector3fc value) {
		set(uniformType.getName(), value);
	}

	/**
	 * Sets a boolean uniform
	 * 
	 * @param uniformName
	 *            name of the uniform
	 * @param value
	 *            value of the boolean
	 */
	public void set(String uniformName, boolean value) {
		set(uniformName, value ? 1 : 0);
	}

	/**
	 * Sets a boolean uniform
	 * 
	 * @param uniformType
	 *            type of the uniform
	 * @param value
	 *            value of the boolean
	 */
	public void set(UniformType uniformType, boolean value) {
		set(uniformType.getName(), value);
	}

	/**
	 * Sets a int uniform
	 * 
	 * @param uniformName
	 *            name of the uniform
	 * @param value
	 *            value of the int
	 */
	public void set(String uniformName, int value) {
		Uniform uniform = getUniform(uniformName);
		GL20.glUniform1i(uniform.location, value);
		// Keep the value cached
		uniform.value = value;
	}

	/**
	 * Sets a int uniform
	 * 
	 * @param uniformType
	 *            type of the uniform
	 * @param value
	 *            value of the int
	 */
	public void set(UniformType uniformType, int value) {
		set(uniformType.getName(), value);
	}

	/**
	 * Sets a float uniform
	 * 
	 * @param uniformName
	 *            name of the uniform
	 * @param value
	 *            value of the float
	 */
	public void set(String uniformName, float value) {
		Uniform uniform = getUniform(uniformName);
		GL20.glUniform1f(uniform.location, value);
		// Keep the value cached
		uniform.value = value;
	}

	/**
	 * Sets a float uniform
	 * 
	 * @param uniformType
	 *            type of the uniform
	 * @param value
	 *            value of the float
	 */
	public void set(UniformType uniformType, float value) {
		set(uniformType.getName(), value);
	}

	/**
	 * Sets a 4D matrix uniform
	 * 
	 * @param uniformName
	 *            name of the uniform
	 * @param value
	 *            value of the 4D matrix
	 */
	public void set(String uniformName, Matrix4fc value) {
		Uniform uniform = getUniform(uniformName);
		GL20.glUniformMatrix4fv(uniform.location, false, value.get(_fb));
		// Keep the value cached
		uniform.value = value;
	}

	/**
	 * Sets a 4D matrix uniform
	 * 
	 * @param uniformType
	 *            type of the uniform
	 * @param value
	 *            value of the 4D matrix
	 */
	public void set(UniformType uniformType, Matrix4fc value) {
		set(uniformType.getName(), value);
	}

	/**
	 * Sets a texture uniform
	 * 
	 * @param uniformType
	 *            type of the uniform
	 * @param texture
	 *            the texture to set
	 * @param sort
	 *            which texture slot, e.g. 0, 1, 2
	 */
	public void setTexture(UniformType uniformType, Texture texture, int sort) {
		if (sort < 0 || sort >= _textureIds.length)
			throw new RuntimeException(
					"Texture sort value is not within the range [0 - " + _textureIds.length + "]: " + sort);

		if (texture == null) {
			clearTexture(uniformType);
			_textureIds[sort] = NULL_TEXTURE;
			return;
		}

		// We want to assign the sort value to the uniform texture slot
		set(uniformType.getName(), sort);

		// Only if the texture hasn't been bound to this slot before
		if (_textureIds[sort] != texture.getTextureId()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + sort);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		}
	}

	/**
	 * Used to clear the bound texture if the texture uniform type passed in has
	 * been previously set
	 * 
	 * @param uniformType
	 *            type of the uniform
	 */
	public void clearTexture(UniformType uniformType) {
		if (!hasUniform(uniformType.getName()))
			return;

		Uniform texture = getUniform(uniformType.getName());

		// If we had a previous value, clear it
		if (texture.value != null) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
	}

	/**
	 * Registers a uniform (i.e. global variable) for use within our shaders
	 * 
	 * @param uniformName
	 *            the name of the uniform variable to register
	 * @throws Exception
	 */
	protected void register(int programId, String uniformName) throws Exception {
		// Try to find the variable name from within our shader code
		int location = GL20.glGetUniformLocation(programId, uniformName);
		if (location < 0) {
			throw new Exception("Could not find uniform: " + uniformName);
		}
		// We found it, so add it to the list of uniforms
		_uniformLocations.put(uniformName, new Uniform(location));
	}

	/**
	 * Returns a uniform location from the set of cached uniforms, if it doesn't
	 * exist a runtime exception is thrown
	 * 
	 * @param uniformName
	 *            the name of the uniform to retrieve from the cache
	 * @return the uniform representing the uniformName passed in
	 */
	private Uniform getUniform(String uniformName) {
		Uniform uniform = _uniformLocations.get(uniformName);
		if (uniform == null) {
			throw new RuntimeException("Uniform [" + uniformName + "] has not been created");
		}
		return uniform;
	}

	/**
	 * Private uniform class to hold the location and value of a uniform
	 * 
	 * @author Brandon Porter
	 *
	 */
	private static class Uniform {
		public final int location;
		public Object value = null;

		/**
		 * Constructs a new uniform to hold the registered location
		 * 
		 * @param location
		 *            the location the particular uniform is registered
		 */
		public Uniform(int location) {
			this.location = location;
		}
	}
}
