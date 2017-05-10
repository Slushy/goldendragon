package engine.scene.gameobjects;

import org.joml.Matrix4f;

import engine.scene.GameObject;
import engine.scene.Transform;
import engine.system.Defaults;
import engine.utils.math.MatrixUtils;

/**
 * The camera game object. For now there can be only one camera: MAIN
 * 
 * @author Brandon Porter
 *
 */
public final class Camera extends GameObject {
	/**
	 * Main camera
	 */
	public static final Camera MAIN = new Camera();

	// Main variables to define our camera viewport
	public float FIELD_OF_VIEW = Defaults.Camera.FIELD_OF_VIEW;
	public float FRUSTUM_NEAR = Defaults.Camera.FRUSTUM_NEAR;
	public float FRUSTUM_FAR = Defaults.Camera.FRUSTUM_FAR;

	// Matrices to deal with the view and projection within the world
	private final Matrix4f _viewMatrix = new Matrix4f();
	private final Matrix4f _projectionMatrix = new Matrix4f();

	/**
	 * Constructs a camera game object
	 */
	private Camera() {
		super("Camera");
	}

	/**
	 * Moves the camera game object in relation to its current position
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void move(float dx, float dy, float dz) {
		Transform transform = this.getTransform();

		float deg90Rad = 90;
		float offsetX = 0;
		float offsetY = 0;
		float offsetZ = 0;

		if (dz != 0) {
			offsetX += (float) Math.sin(Math.toRadians(transform.getRotation().y())) * -1.0f * dz;
			offsetZ += (float) Math.cos(Math.toRadians(transform.getRotation().y())) * dz;
		}

		if (dx != 0) {
			offsetX += (float) Math.sin(Math.toRadians(transform.getRotation().y() - deg90Rad)) * -1.0f * dx;
			offsetZ += (float) Math.cos(Math.toRadians(transform.getRotation().y() - deg90Rad)) * dx;
		}

		if (dy != 0) {
			offsetY += dy;
		}

		transform.move(offsetX, offsetY, offsetZ);
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
	 * Updates the view matrix to the current camera position/rotation
	 * 
	 * @return updated view matrix
	 */
	public Matrix4f updateViewMatrix() {
		return MatrixUtils.setCameraViewMatrix(_viewMatrix, getTransform().getPosition(), getTransform().getRotation());
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
		return _projectionMatrix.setPerspective(FIELD_OF_VIEW, aspectRatio, FRUSTUM_NEAR, FRUSTUM_FAR);
	}
}