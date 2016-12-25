package engine.game.objects;

import org.joml.Matrix4f;

import engine.EngineDefaults;

/**
 * A camera is just a way to view the 3D world through a 2D screen
 * 
 * @author brandon.porter
 *
 */
public class Camera {
	public static float FIELD_OF_VIEW = EngineDefaults.FIELD_OF_VIEW;
	public static float Z_NEAR = EngineDefaults.Z_NEAR;
	public static float Z_FAR = EngineDefaults.Z_FAR;

	private final Matrix4f _projectionMatrix = new Matrix4f();
	
	/**
	 * Constructs a camera object
	 */
	public Camera() {
	}
	
	/**
	 * Gets the projection matrix for the camera
	 * @return
	 */
	public Matrix4f getProjectionMatrix() {
		return _projectionMatrix;
	}
	
	/**
	 * Updates the projection matrix for the camera
	 * @param aspectRatio the width/height ratio of the screen
	 * @return
	 */
	public Matrix4f updateProjectionMatrix(float aspectRatio) {
		return _projectionMatrix.setPerspective(FIELD_OF_VIEW, aspectRatio, Z_NEAR, Z_FAR);
	}
}
