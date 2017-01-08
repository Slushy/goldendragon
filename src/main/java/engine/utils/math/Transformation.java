package engine.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.common.Defaults;
import engine.common.Transform;

/**
 * Holds instances of matrices for calculations to reduce the amount of
 * instantiated matrices our game uses
 * 
 * @author brandon.porter
 *
 */
public final class Transformation {
	private static final Matrix4f WORLD_MATRIX = new Matrix4f();
	private static final Matrix4f WORLD_VIEW_MATRIX = new Matrix4f();

	private static final Matrix4f LIGHT_VIEW_MATRIX = new Matrix4f();
	private static final Vector3f DIRECTIONAL_LIGHT = new Vector3f();

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
		// return WORLD_MATRIX.translationRotateScale(position.x, position.y,
		// position.z, rotation.x, rotation.y, rotation.z,
		// rotation.w, scale, scale, scale);
		return WORLD_MATRIX.translation(position).rotateX((float) Math.toRadians(-rotation.x))
				.rotateY((float) Math.toRadians(-rotation.y)).rotateZ((float) Math.toRadians(-rotation.z)).scale(scale);
	}

	/**
	 * Transforms the local rotation of the directional light to a position in
	 * camera world space
	 * 
	 * @param lightRotation
	 *            the rotation of the directional light
	 * @param viewMatrix
	 *            the view matrix of the camera
	 * @return new position of directional light
	 */
	public static Vector3f getDirectionalLightDirection(Vector3f lightRotation, Matrix4f viewMatrix) {
		// NOTE: This is the math behind calculating the new xyz position of the rotation of the point <0, 0, z> around the origin 
		
//		float xValue = (float) ((float) - Math.sin(Math.toRadians(lightRotation.y)) * Math.cos(Math.toRadians(lightRotation.x))) * Defaults.Lighting.DIRECTIONAL_Z_POSITION;
//		float yValue = (float) Math.sin(Math.toRadians(lightRotation.x)) * Defaults.Lighting.DIRECTIONAL_Z_POSITION;
//		float zValue = (float) ((float) 
//				Math.sin(Math.toRadians(lightRotation.y))*xValue + Math.cos(Math.toRadians(lightRotation.y))*-Math.sin(Math.toRadians(lightRotation.x)) 
//				*yValue +
//				((float) Math.cos(Math.toRadians(lightRotation.y)) * Math.cos(Math.toRadians(lightRotation.x)) * Defaults.Lighting.DIRECTIONAL_Z_POSITION));
		
		// Rotates a point around the origin (this is what the sun does). We
		// store it in a view matrix so we can multiply it by the cameras
		// rotation to get the final rotation of the directional light.
		//
		// NOTE: The sun (our directional light) will always rotate around the Y axis (unlike our camera which rotates around the X)
		// NOTE 2: "rotationY clears out any existing data by making it an identity matrix and then setting the y rotation. where as
		//         "rotateX" applies the rotation to any existing data
		LIGHT_VIEW_MATRIX.rotationY(-(float) Math.toRadians(lightRotation.y)).rotateX((float) Math.toRadians(lightRotation.x))
		.translate(0, 0, Defaults.Lighting.DIRECTIONAL_Z_POSITION);
		
		// Temporarily clear the main cameras current translation since
		// we do not want it to affect the directional light position 
		// (we only want rotation)
		float posX = viewMatrix.m30();
		float posY = viewMatrix.m31();
		float posZ = viewMatrix.m32();
		viewMatrix.setTranslation(0, 0, 0);
		
		// Multiplies the camera's view matrix by the light view matrix to get
		// our final position
		viewMatrix.mul(LIGHT_VIEW_MATRIX, LIGHT_VIEW_MATRIX);
		
		// Restore the cameras translation
		viewMatrix.setTranslation(posX, posY, posZ);

		// Gets the new directional light position based off of its
		// and the cameras rotation (This should already be normalized, if it were
		// not we would have to do DIRECTIONAL_LIGHT.normalize() before returning)
		return LIGHT_VIEW_MATRIX.getTranslation(DIRECTIONAL_LIGHT);
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
