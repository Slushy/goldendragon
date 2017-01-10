package engine.utils.math;

import org.joml.Vector3f;

/**
 * Utility helper class for common vector manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public final class VectorUtils {
	public static final int VECTOR_3D_SIZE = 3;
	public static final int VECTOR_2D_SIZE = 2;
	
	/*
	 * Prevent manual instantiation
	 */
	private VectorUtils() {
	}

	/**
	 * Helper function to set the x, y, z coordinates of a vector
	 * 
	 * @param vector
	 * @param x
	 * @param y
	 * @param z
	 * @return the updated vector
	 */
	public static Vector3f setVector(Vector3f vector, float x, float y, float z) {
		vector.x = x;
		vector.y = y;
		vector.z = z;
		return vector;
	}
}
