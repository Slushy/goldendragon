package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;

/**
 * Utility helper class for common matrix manipulation/calculations
 * 
 * @author brandon.porter
 *
 */
public final class MatrixUtils {
	public static final int MATRIX_4D_SIZE = 16;
	public static final int MATRIX_3D_SIZE = 9;

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

	/**
	 * Compares the two matrixes for equality in instance or values
	 * 
	 * @param m1
	 * @param m2
	 * @return true if the matrixes are the same or have the same values
	 */
	public static boolean compare(Matrix4fc m1, Matrix4fc m2) {
		if (m1 == m2)
			return true;

		if (m1 == null || m2 == null)
			return false;

		return m1.m00() == m2.m00() && m1.m01() == m2.m01() && m1.m02() == m2.m02() && m1.m03() == m2.m03()
				&& m1.m10() == m2.m10() && m1.m11() == m2.m11() && m1.m12() == m2.m12() && m1.m13() == m2.m13()
				&& m1.m20() == m2.m20() && m1.m21() == m2.m21() && m1.m22() == m2.m22() && m1.m23() == m2.m23()
				&& m1.m30() == m2.m30() && m1.m31() == m2.m31() && m1.m32() == m2.m32() && m1.m33() == m2.m33();
	}
}
