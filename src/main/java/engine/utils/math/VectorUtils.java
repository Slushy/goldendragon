package engine.utils.math;

import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Utility helper class for common vector manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public final class VectorUtils {
	public static final int VECTOR_4D_SIZE = 4;
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
	
	/**
	 * Compares the two vectors for equality in instance or values
	 * 
	 * @param vector1
	 * @param vector2
	 * @return true if the vectors are the same or have the same values
	 */
	public static boolean compare(Vector3fc vector1, Vector3fc vector2) {
		if (vector1 == vector2)
			return true;

		if (vector1 == null || vector2 == null)
			return false;

		return vector1.x() == vector2.x() && vector1.y() == vector2.y() && vector1.z() == vector2.z();
	}
}
