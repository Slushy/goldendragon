package engine.utils.math;

import org.joml.Quaternionf;

/**
 * Utility helper class for common quaternion manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public class QuaternionUtils {

	/*
	 * Prevent manual instantiation
	 */
	private QuaternionUtils() {
	}

	/**
	 * Helper function to set the x, y, z, w coordinates of a quaternion
	 * 
	 * @param quaternion
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return the updated quaternion
	 */
	public static Quaternionf setQuaternion(Quaternionf quaternion, float x, float y, float z, float w) {
		quaternion.x = x;
		quaternion.y = y;
		quaternion.z = z;
		quaternion.w = w;
		return quaternion;
	}
}
