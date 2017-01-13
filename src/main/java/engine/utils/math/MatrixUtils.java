package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3fc;

/**
 * Utility helper class for common matrix manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public final class MatrixUtils {

	/*
	 * Static class
	 */
	private MatrixUtils() {
	}

	/**
	 * Helper function to set the 3D world matrix from position, rotation and
	 * scale vectors
	 * 
	 * @param matrix
	 *            the matrix to transform
	 * @param position
	 *            position vector to translate the matrix
	 * @param rotation
	 *            rotation vector to rotate the matrix
	 * @param scale
	 *            scale vector to scale the matrix
	 * @return the update matrix
	 */
	public static Matrix4f setWorldMatrix(Matrix4f matrix, Vector3fc position, Vector3fc rotation, Vector3fc scale) {
		return matrix.translation(position).rotateY((float) Math.toRadians(-rotation.y()))
				.rotateX((float) Math.toRadians(-rotation.x())).rotateZ((float) Math.toRadians(-rotation.z()))
				.scale(scale);
	}

	/**
	 * Helper function to set the rotation and position of an object view matrix
	 * 
	 * NOTE: An object view matrix always rotates around the Y-axis first with a
	 * negative rotation
	 * 
	 * @param matrix
	 *            the matrix to transform
	 * @param position
	 *            position vector of the object
	 * @param rotation
	 *            rotation vector of the object
	 * @return the updated matrix
	 */
	public static Matrix4f setObjectViewMatrix(Matrix4f matrix, Vector3fc position, Vector3fc rotation) {
		// Do rotation first so rotation applies over its position
		// We do "rotation" instead of "rotate" to clear out any previous set
		// data
		return matrix.rotationYXZ((float) Math.toRadians(-rotation.y()), (float) Math.toRadians(-rotation.x()),
				(float) Math.toRadians(-rotation.z())).translate(position.x(), position.y(), position.z());
	}

	/**
	 * Helper function to set the rotation and position of a camera view matrix
	 * 
	 * NOTE: A camera view matrix always rotates around the X-axis first with a
	 * native translation
	 * 
	 * @param matrix
	 *            the matrix to transform
	 * @param position
	 *            position vector of the camera
	 * @param rotation
	 *            rotation vector of the camera
	 * @return the updated matrix
	 */
	public static Matrix4f setCameraViewMatrix(Matrix4f matrix, Vector3fc position, Vector3fc rotation) {
		// Do rotation first so rotation applies over its position
		// We do "rotation" instead of "rotate" to clear out any previous set
		// data
		return matrix.rotationXYZ((float) Math.toRadians(rotation.x()), (float) Math.toRadians(rotation.y()),
				(float) Math.toRadians(rotation.z())).translate(-position.x(), -position.y(), -position.z());
	}
}
