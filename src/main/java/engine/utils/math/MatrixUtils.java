package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Utility helper class for common matrix manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public final class MatrixUtils {

	/*
	 * Prevent manual instantiation
	 */
	private MatrixUtils() {
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
	public static Matrix4f setObjectViewMatrix(Matrix4f matrix, Vector3f position, Vector3f rotation) {
		// Do rotation first so rotation applies over its position
		// We do "rotation" instead of "rotate" to clear out any previous set
		// data
		return matrix.rotationYXZ((float) Math.toRadians(-rotation.y), (float) Math.toRadians(-rotation.x),
				(float) Math.toRadians(-rotation.z)).translate(position.x, position.y, position.z);
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
	public static Matrix4f setCameraViewMatrix(Matrix4f matrix, Vector3f position, Vector3f rotation) {
		// Do rotation first so rotation applies over its position
		// We do "rotation" instead of "rotate" to clear out any previous set
		// data
		return matrix.rotationXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y),
				(float) Math.toRadians(rotation.z)).translate(-position.x, -position.y, -position.z);
	}

	/**
	 * To transform a matrix without being offset by translation, we temporarily
	 * clear the view matrix's current translation, do the transform, and reset
	 * the translation
	 * 
	 * @param matrixToTransform
	 *            the matrix to be transformed by the view matrix
	 * @param viewMatrix
	 *            the matrix to temporarily remove translation to transform the
	 *            other matrix
	 * @return the matrixToTransform instance is returned
	 */
	public static Matrix4f transformMatrixWithoutTranslation(Matrix4f matrixToTransform, Matrix4f viewMatrix) {
		// Temporarily clear the main cameras current translation since
		// we do not want it to affect the directional light position
		// (we only want rotation)
		float posX = viewMatrix.m30();
		float posY = viewMatrix.m31();
		float posZ = viewMatrix.m32();
		viewMatrix.setTranslation(0, 0, 0);

		// Multiplies the camera's view matrix by the light view matrix to get
		// our final position
		viewMatrix.mul(matrixToTransform, matrixToTransform);

		// Restore the cameras translation
		viewMatrix.setTranslation(posX, posY, posZ);

		return matrixToTransform;
	}
}
