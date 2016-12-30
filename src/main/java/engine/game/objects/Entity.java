package engine.game.objects;

import org.joml.Vector3f;

import engine.utils.math.VectorUtils;

/**
 * A base class for every object with position & rotation
 * 
 * @author brandon.porter
 *
 */
public abstract class Entity {
	// Keeps track of entity movements
	private final Vector3f _position;
	// TODO: Change rotation to be a quaternion instead of vector
	private final Vector3f _rotation;

	/**
	 * Constructs a new entity
	 * 
	 * @param position
	 *            default starting position
	 * @param rotation
	 *            default starting rotation
	 */
	public Entity(Vector3f position, Vector3f rotation) {
		this._position = position;
		this._rotation = rotation;
	}

	/**
	 * Gets the current entity position
	 * 
	 * @return current position
	 */
	public Vector3f getPosition() {
		return _position;
	}

	/**
	 * Sets the new entity position
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
	 * Sets the new X position of the entity
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setPosX(float x) {
		_position.x = x;
	}

	/**
	 * Sets the new Y position of the entity
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setPosY(float y) {
		_position.y = y;
	}

	/**
	 * Sets the new Z position of the entity
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
	 * Gets the current entity rotation
	 * 
	 * @return current rotation
	 */
	public Vector3f getRotation() {
		return _rotation;
	}

	/**
	 * Sets the new entity rotation
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
	 * Sets the new X rotation of the entity
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setRotX(float x) {
		_rotation.x = x;
	}

	/**
	 * Sets the new Y rotation of the entity
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setRotY(float y) {
		_rotation.y = y;
	}

	/**
	 * Sets the new Z rotation of the entity
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

}
