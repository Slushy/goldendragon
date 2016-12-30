package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Utility helper class for common matrix manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public class MatrixUtils {

	/*
	 * Prevent manual instantiation
	 */
	private MatrixUtils() {
	}

	/**
	 * Helper function to set the rotation and position of a view matrix
	 * 
	 * @param matrix
	 * @param position
	 * @param rotation
	 * @return the updated matrix
	 */
	public static Matrix4f setViewMatrix(Matrix4f matrix, Vector3f position, Vector3f rotation) {
		// Do rotation first so rotation applies over its position
		// We do rotationX to first rotate the entire matrix around the x-axis as a starting
		// base, then we do rotateY instead of rotationY to keep the existing x-rotation
		//return matrix.rotationX(rotation.x).rotateY(rotation.y).translate(-position.x, -position.y, -position.z);
		return matrix.rotationX((float) Math.toRadians(rotation.x))
				.rotateY((float) Math.toRadians(rotation.y))
				.translate(-position.x, -position.y, -position.z);
	}
}
