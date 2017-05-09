package engine.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * A bunch of utility methods
 * 
 * @author brandon.porter
 *
 */
public final class MemoryUtils {
	public static final int FLOAT_SIZE_BYTES = 4;

	// Static class
	private MemoryUtils() {
	}

	/**
	 * Loads an array of floats into a float buffer
	 * 
	 * @param data
	 * @return
	 */
	public static FloatBuffer loadBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}

	/**
	 * Loads an array of ints into an integer buffer
	 * 
	 * @param data
	 * @return
	 */
	public static IntBuffer loadBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
}
