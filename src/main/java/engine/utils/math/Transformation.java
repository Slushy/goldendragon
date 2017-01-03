package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.common.Transform;

/**
 * Holds instances of matrices for calculations to reduce the amount of
 * instantiated matrices our game uses
 * 
 * @author brandon.porter
 *
 */
public class Transformation {
	private static final Matrix4f WORLD_MATRIX = new Matrix4f();
	private static final Matrix4f WORLD_VIEW_MATRIX = new Matrix4f();
	
	/*
	 * Prevent manual instantiation
	 */
	private Transformation() {
	}

	/**
	 * Returns a matrix representing the position, rotation and scale passed in
	 * 
	 * @param position
	 * @param rotation
	 * @param scale
	 * @return transformed matrix
	 */
	public static Matrix4f buildWorldMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
//		return WORLD_MATRIX.translationRotateScale(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z,
//			rotation.w, scale, scale, scale);
		return WORLD_MATRIX.translation(position)
				.rotateX((float)Math.toRadians(-rotation.x))
				.rotateY((float)Math.toRadians(-rotation.y))
				.rotateZ((float)Math.toRadians(-rotation.z))
				.scale(scale);
	}                                                                                                                                                                                                                                     

	/**
	 * Returns a matrix representing the position, rotation and scale of the
	 * game object
	 * 
	 * @param transform
	 * @return transformed matrix
	 */
	public static Matrix4f buildWorldMatrix(Transform transform) {
		return buildWorldMatrix(transform.getPosition(), transform.getRotation(), transform.getScale());
	}

	/**
	 * Returns a matrix representing the position, rotation and scale of the
	 * game object in relation to the passed in view matrix
	 * 
	 * @param transform
	 * @param viewMatrix
	 * @return
	 */
	public static Matrix4f buildWorldViewMatrix(Transform transform, Matrix4f viewMatrix) {
		return viewMatrix.mul(buildWorldMatrix(transform), WORLD_VIEW_MATRIX);
	}
}
