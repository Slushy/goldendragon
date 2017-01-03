package engine.common;

import org.joml.Vector3f;

import engine.utils.math.VectorUtils;

/**
 * Defines the basic properties of transformations every game object in the
 * engine will be required to have
 * 
 * @author Brandon Porter
 *
 */
public class Transform extends Component {
	private final Vector3f _position = new Vector3f();
	private final Vector3f _rotation = new Vector3f();
	private final Vector3f _scale = new Vector3f(1, 1, 1);

	/**
	 * Constructs a transform component
	 */
	public Transform() {
		super("Transform");
	}

	/**
	 * Gets the current transform position
	 * 
	 * @return current position
	 */
	public Vector3f getPosition() {
		return _position;
	}

	/**
	 * Sets the new transform position
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param z
	 *            z-coordinate
	 */
	public void setPosition(float x, float y, float z) {
		VectorUtils.setVector(_position, x, y, z);
	}

	/**
	 * Sets the new X position of the transform
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setPosX(float x) {
		_position.x = x;
	}

	/**
	 * Sets the new Y position of the transform
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setPosY(float y) {
		_position.y = y;
	}

	/**
	 * Sets the new Z position of the transform
	 * 
	 * @param z
	 *            z-coordinate
	 */
	public void setPosZ(float z) {
		_position.z = z;
	}

	/**
	 * Increases the position by delta amount
	 * 
	 * @param dx
	 *            x-axis delta
	 * @param dy
	 *            y-axis delta
	 * @param dz
	 *            z-axis delta
	 */
	public void move(float dx, float dy, float dz) {
		_position.x += dx;
		_position.y += dy;
		_position.z += dz;
	}

	/**
	 * Gets the current transform rotation
	 * 
	 * @return current rotation
	 */
	public Vector3f getRotation() {
		return _rotation;
	}

	/**
	 * Sets the new transform rotation
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param z
	 *            z-coordinate
	 */
	public void setRotation(float x, float y, float z) {
		VectorUtils.setVector(_rotation, x, y, z);
	}

	/**
	 * Sets the new X rotation of the transform
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setRotX(float x) {
		_rotation.x = x;
	}

	/**
	 * Sets the new Y rotation of the transform
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setRotY(float y) {
		_rotation.y = y;
	}

	/**
	 * Sets the new Z rotation of the transform
	 * 
	 * @param z
	 *            z-coordinate
	 */
	public void setRotZ(float z) {
		_rotation.z = z;
	}

	/**
	 * Increases the rotation by delta amount
	 * 
	 * @param dx
	 *            x-axis delta
	 * @param dy
	 *            y-axis delta
	 * @param dz
	 *            z-axis delta
	 */
	public void rotate(float dx, float dy, float dz) {
		_rotation.x += dx;
		_rotation.y += dy;
		_rotation.z += dz;
	}

	/**
	 * Gets the current transform scale
	 * 
	 * @return current scale
	 */
	public Vector3f getScale() {
		return _scale;
	}

	/**
	 * Sets the transform scale uniformly across each axis
	 * 
	 * @param scale
	 *            how much to scale on the xyz axis
	 */
	public void setScale(float scale) {
		setScale(scale, scale, scale);
	}

	/**
	 * Sets the new transform scale
	 * 
	 * @param x
	 *            how much to scale on x axis
	 * @param y
	 *            how much to scale on y axis
	 * @param z
	 *            how much to scale on z axis
	 */
	public void setScale(float x, float y, float z) {
		VectorUtils.setVector(_scale, x, y, z);
	}

	/**
	 * Sets the new X scale of the transform
	 * 
	 * @param x
	 *            how much to scale on x axis
	 */
	public void setScaleX(float x) {
		_scale.x = x;
	}

	/**
	 * Sets the new Y scale of the transform
	 * 
	 * @param y
	 *            how much to scale on y axis
	 */
	public void setScaleY(float y) {
		_scale.y = y;
	}

	/**
	 * Sets the new Z scale of the transform
	 * 
	 * @param z
	 *            how much to scale on z axis
	 */
	public void setScaleZ(float z) {
		_scale.z = z;
	}
	
	@Override
	public String toString() {
		return String.format("Translation: %s, Rotation: %s, Scale: %s", _position, _rotation, _scale);
	}
}
