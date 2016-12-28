package engine.game.objects;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import engine.EngineDefaults;
import engine.utils.math.MatrixUtils;

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
	
	/**
	 * Move offset position in relation to current position
	 * @param offsetX
	 * @param offsetY
	 * @param offsetZ
	 */
	public void movePosition(float offsetX, float offsetY, float offsetZ) {
		float deg90Rad = 90;
		if (offsetZ != 0) {
			getPosition().x += (float)Math.sin(Math.toRadians(getRotation().y))* -1.0f * offsetZ;
			getPosition().z += (float)Math.cos(Math.toRadians(getRotation().y)) * offsetZ;
		}
		if (offsetX != 0) {
			getPosition().x += (float)Math.sin(Math.toRadians(getRotation().y - deg90Rad))* -1.0f * offsetX;
			getPosition().z += (float)Math.cos(Math.toRadians(getRotation().y - deg90Rad)) * offsetX;
		}
		if (offsetY != 0) {
			getPosition().y += offsetY;
		}
		
	}
	
	/**
	 * Move camera rotation by the offset (in radians)
	 * @param offsetX
	 * @param offsetY
	 * @param offsetZ
	 */
	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		getRotation().x += offsetX;
		getRotation().y += offsetY;
		getRotation().z += offsetZ;
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
	 * Updates the view matrix to the current camera position/rotation
	 * 
	 * @return updated view matrix
	 */
	public Matrix4f updateViewMatrix() {
		return MatrixUtils.setViewMatrix(_viewMatrix, getPosition(), getRotation());
	}
}
