package engine.common.components;

import org.joml.Matrix4f;

import engine.common.Component;
import engine.common.Defaults;

/**
 * A camera component represents a way to view the world through a screen
 * 
 * @author brandon.porter
 *
 */
public class CameraProjection extends Component {
	public static float FIELD_OF_VIEW = Defaults.Camera.FIELD_OF_VIEW;
	public static float FRUSTUM_NEAR = Defaults.Camera.FRUSTUM_NEAR;
	public static float FRUSTUM_FAR = Defaults.Camera.FRUSTUM_FAR;

	// Matrices to deal with projection within the world
	private final Matrix4f _projectionMatrix = new Matrix4f();

	// TODO: Allow roll, pitch & yaw changes to rotation 
	
	/**
	 * Constructs a camera object
	 */
	public CameraProjection() {
		super("Camera Projection");
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
