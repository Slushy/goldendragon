package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.common.Defaults;
import engine.common.Transform;

/**
 * Holds instances of matrices and vectors for calculations to reduce the amount
 * of instantiated math objects our game uses
 * 
 * @author brandon.porter
 *
 */
public final class Transformation {
	private final Matrix4f WORLD_MATRIX = new Matrix4f();
	private final Matrix4f WORLD_VIEW_MATRIX = new Matrix4f();

	private final Matrix4f LIGHT_VIEW_MATRIX = new Matrix4f();
	private final Vector3f VIEW_VECTOR3f = new Vector3f();
	private final Vector4f VIEW_VECTOR4f = new Vector4f();

	/**
	 * Constructs a new transformation instance
	 */
	public Transformation() {
	}

	/**
	 * Returns a matrix representing the position, rotation and scale passed in
	 * 
	 * @param position
	 * @param rotation
	 * @param scale
	 * @return transformed matrix in view space
	 */
	public Matrix4f buildWorldMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
		// return WORLD_MATRIX.translationRotateScale(position.x, position.y,
		// position.z, rotation.x, rotation.y, rotation.z,
		// rotation.w, scale, scale, scale);
		return WORLD_MATRIX.translation(position).rotateY((float) Math.toRadians(-rotation.y))
				.rotateX((float) Math.toRadians(-rotation.x)).rotateZ((float) Math.toRadians(-rotation.z)).scale(scale);
	}

	/**
	 * Returns a matrix representing the position, rotation and scale of the
	 * game object
	 * 
	 * @param transform
	 * @return transformed matrix in view space
	 */
	public Matrix4f buildWorldMatrix(Transform transform) {
		return buildWorldMatrix(transform.getPosition(), transform.getRotation(), transform.getScale());
	}

	/**
	 * Returns a matrix representing the position, rotation and scale of the
	 * game object in relation to the passed in view matrix
	 * 
	 * @param transform
	 * @param viewMatrix
	 * @return transformed matrix in view space
	 */
	public Matrix4f buildWorldViewMatrix(Transform transform, Matrix4f viewMatrix) {
		return viewMatrix.mul(buildWorldMatrix(transform), WORLD_VIEW_MATRIX);
	}
	
	/**
	 * Returns the facing direction of a transform in view space
	 * 
	 * @param rotation
	 *            the local rotation of the transform
	 * @param viewMatrix
	 *            the view matrix of the camera
	 * @return facing direction of the transform in relation to the camera
	 */
	public Vector3f getFacingDirection(Vector3f rotation, Matrix4f viewMatrix) {
		// Apply the rotation to a preset position
		MatrixUtils.setObjectViewMatrix(LIGHT_VIEW_MATRIX, Defaults.Scene.OBJECT_FACING_DIRECTION, rotation);

		// Temporarily clear the main cameras current translation since
		// we do not want it to affect the light position
		// (we only want rotation)
		MatrixUtils.transformMatrixWithoutTranslation(LIGHT_VIEW_MATRIX, viewMatrix);

		// Gets the new light direction based off of its and the cameras
		// rotation (This should already be normalized, if it were not we would
		// have to do VIEW_VECTOR3f.normalize() before returning)
		return LIGHT_VIEW_MATRIX.getTranslation(VIEW_VECTOR3f);
	}

	/**
	 * Converts a local position/direction vector to a 4-component vector to
	 * multiply against the view matrix (our camera) to convert it into view
	 * space. If we include translation in the calculation that means we do also
	 * want the vector to be changed based on the camera position in the world
	 * space. After the transformation we return back the xyz component as a
	 * 3-component vector.
	 * 
	 * i.e. if you want an object (that holds the specified local vector) to get
	 * closer/further away from the camera based on where the camera moves to in
	 * the world, then you want to include translation. A sun or a skybox is an
	 * example where it should be false because the sun shouldn't appear to get
	 * "closer" when the camera moves towards it.
	 * 
	 * @param localSpaceVector
	 *            the vector in local space to convert to view space
	 * @param viewMatrix
	 *            the view space to convert the vector to (usually our camera)
	 * @param includeTranslation
	 *            whether or not to include translation in the local-view space
	 *            transformation
	 * @return a "shared" 3-component vector representing the transformed local
	 *         vector in view space
	 */
	public Vector3f buildWorldViewVector(Vector3f localSpaceVector, Matrix4f viewMatrix, boolean includeTranslation) {
		VIEW_VECTOR4f.set(localSpaceVector, includeTranslation ? 1 : 0);
		VIEW_VECTOR4f.mul(viewMatrix);

		// Set back the new position in view-space
		return VIEW_VECTOR3f.set(VIEW_VECTOR4f.x, VIEW_VECTOR4f.y, VIEW_VECTOR4f.z);
	}
}
