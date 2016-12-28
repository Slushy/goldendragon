package engine.game.objects;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import engine.utils.math.QuaternionUtils;
import engine.utils.math.VectorUtils;

/**
 * A base class for every object with position & rotation
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
	 * Gets the current camera position
	 * 
	 * @return current position
	 */
	public Vector3f getPosition() {
		return _position;
	}

	/**
	 * Sets the new camera position
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
	 * Gets the current camera rotation
	 * 
	 * @return current rotation
	 */
	public Vector3f getRotation() {
		return _rotation;
	}

	/**
	 * Sets the new camera rotation
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param z
	 *            z-coordinate
	 */
	public void setRotation(float x, float y, float z, float w) {
		VectorUtils.setVector(_rotation, x, y, z);
	}
}
