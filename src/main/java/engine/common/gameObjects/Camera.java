package engine.common.gameObjects;

import org.joml.Matrix4f;

import engine.common.GameObject;
import engine.common.components.CameraProjection;
import engine.common.components.Transform;
import engine.utils.math.MatrixUtils;

public class Camera extends GameObject {
	private final CameraProjection _cameraProjection = new CameraProjection();
	// Matrices to deal with the view within the world
	private final Matrix4f _viewMatrix = new Matrix4f();

	/**
	 * Constructs a camera game object
	 */
	public Camera() {
		super("Camera");

		// Every camera needs a camera projection
		this.addComponent(_cameraProjection);
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
			offsetX += (float) Math.sin(Math.toRadians(transform.getRotation().y)) * -1.0f * dz;
			offsetZ += (float) Math.cos(Math.toRadians(transform.getRotation().y)) * dz;
		}

		if (dx != 0) {
			offsetX += (float) Math.sin(Math.toRadians(transform.getRotation().y - deg90Rad)) * -1.0f * dx;
			offsetZ += (float) Math.cos(Math.toRadians(transform.getRotation().y - deg90Rad)) * dx;
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
		return MatrixUtils.setViewMatrix(_viewMatrix, getTransform().getPosition(), getTransform().getRotation());
	}

	/**
	 * TODO: REMOVE, ONLY TEMPORARY Gets the projection matrix for the camera
	 * 
	 * @return projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		return _cameraProjection.getProjectionMatrix();
	}

	/**
	 * TODO: REMOVE, ONLY TEMPORARY Updates the projection matrix for the camera
	 * 
	 * @param aspectRatio
	 *            the width/height ratio of the screen
	 * @return updated projection matrix
	 */
	public Matrix4f updateProjectionMatrix(float aspectRatio) {
		return _cameraProjection.updateProjectionMatrix(aspectRatio);
	}
}