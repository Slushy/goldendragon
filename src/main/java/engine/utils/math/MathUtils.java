package engine.utils.math;

/**
 * Utility helper class for common math manipulation/calculations not provided
 * by Java's Math.*
 * 
 * @author Brandon Porter
 *
 */
public final class MathUtils {

	/**
	 * Clamps a float value between two values. If the value < min, it will
	 * return min, if the value > max, it will return max. Otherwise it will
	 * return the original float value.
	 * 
	 * @param val
	 *            the value to clamp
	 * @param min
	 *            the min clamping value
	 * @param max
	 *            the max clamping value
	 * @return the clamped value
	 */
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
}
