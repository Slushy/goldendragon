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
public class Utils {

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
