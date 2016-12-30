package engine.common.components;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.common.Defaults;
import engine.game.objects.Entity;
import engine.utils.math.MatrixUtils;

/**
 * A camera is just a way to view the 3D world through a 2D screen
 * 
 * @author brandon.porter
 *
 */
public class Camera extends Entity {
	public static float FIELD_OF_VIEW = Defaults.Camera.FIELD_OF_VIEW;
	public static float FRUSTUM_NEAR = Defaults.Camera.FRUSTUM_NEAR;
	public static float FRUSTUM_FAR = Defaults.Camera.FRUSTUM_FAR;

	// Matrices to deal with view and projection within the world
	private final Matrix4f _projectionMatrix = new Matrix4f();
	private final Matrix4f _viewMatrix = new Matrix4f();

	// TODO: Allow roll, pitch & yaw changes to rotation 
	
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
		this.updateViewMatrix();
	}
	
	@Override
	public void move(float dx, float dy, float dz) {
		float deg90Rad = 90;
		
		float offsetX = 0;
		float offsetY = 0;
		float offsetZ = 0;
		
		if (dz != 0) {
			offsetX += (float)Math.sin(Math.toRadians(getRotation().y))* -1.0f * dz;
			offsetZ += (float)Math.cos(Math.toRadians(getRotation().y)) * dz;
		}
		
		if (dx != 0) {
			offsetX += (float)Math.sin(Math.toRadians(getRotation().y - deg90Rad))* -1.0f * dx;
			offsetZ += (float)Math.cos(Math.toRadians(getRotation().y - deg90Rad)) * dx;
		}
		
		if (dy != 0) {
			offsetY += dy;
		}
		
		super.move(offsetX, offsetY, offsetZ);
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
		return MatrixUtils.setViewMatrix(_viewMatrix, getPosition(), getRotation());
	}
}
