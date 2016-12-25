package engine.game.objects;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.EngineDefaults;

/**
 * A camera is just a way to view the 3D world through a 2D screen
 * 
 * @author brandon.porter
 *
 */
public class Camera extends Entity {
	public static float FIELD_OF_VIEW = EngineDefaults.FIELD_OF_VIEW;
	public static float Z_NEAR = EngineDefaults.Z_NEAR;
	public static float Z_FAR = EngineDefaults.Z_FAR;

	// Matrices to deal with view and projection within the world
	private final Matrix4f _projectionMatrix = new Matrix4f();
	private final Matrix4f _viewMatrix = new Matrix4f();

	/**
	 * Constructs a camera object
	 */
	public Camera() {
		this(new Vector3f(), new Vector3f());
	}

	/**
	 * Constructs a camera object
	 * 
	 * @param position
	 * @param rotation
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		super(position, rotation);
	}

	/**
	 * Gets the projection matrix for the camera
	 * 
	 * @return projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		return _projectionMatrix;
	}

	/**
	 * Updates the projection matrix for the camera
	 * 
	 * @param aspectRatio
	 *            the width/height ratio of the screen
	 * @return updated projection matrix
	 */
	public Matrix4f updateProjectionMatrix(float aspectRatio) {
		return _projectionMatrix.setPerspective(FIELD_OF_VIEW, aspectRatio, Z_NEAR, Z_FAR);
	}

	/**
	 * Gets the view matrix for the camera
	 * 
	 * @return current view matrix
	 */
	public Matrix4f getViewMatrix() {
		return _viewMatrix;
	}

	/**
	 * Updates the view matrix to the current camera position/rotation TODO:
	 * IMPLEMENT ME
	 * 
	 * @return updated view matrix
	 */
	public Matrix4f updateViewMatrix() {
		// TODO: Implement me
		return _viewMatrix;
	}
}
