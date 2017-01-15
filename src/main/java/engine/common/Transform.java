package engine.common;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import engine.utils.math.MatrixUtils;
import engine.utils.math.VectorUtils;

/**
 * Defines the basic properties of transformations every game object in the
 * engine will be required to have
 * 
 * @author Brandon Porter
 *
 */
public class Transform extends Component {
	private static final String COMPONENT_NAME = "Transform";

	private final Vector3f _position = new Vector3f();
	private final Vector3f _rotation = new Vector3f();
	private final Vector3f _scale = new Vector3f(1, 1, 1);
	private final Matrix4f _localToWorldMatrix = new Matrix4f();

	private boolean _hasChanged = false;

	/**
	 * Constructs a transform component
	 */
	public Transform() {
		super(COMPONENT_NAME);
	}

	/**
	 * Gets the current transform position
	 * 
	 * @return current position
	 */
	public Vector3fc getPosition() {
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
		setChanged();
	}

	/**
	 * Sets the new X position of the transform
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setPosX(float x) {
		_position.x = x;
		setChanged();
	}

	/**
	 * Sets the new Y position of the transform
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setPosY(float y) {
		_position.y = y;
		setChanged();
	}

	/**
	 * Sets the new Z position of the transform
	 * 
	 * @param z
	 *            z-coordinate
	 */
	public void setPosZ(float z) {
		_position.z = z;
		setChanged();
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
		setChanged();
	}

	/**
	 * Gets the current transform rotation
	 * 
	 * @return current rotation
	 */
	public Vector3fc getRotation() {
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
		setChanged();
	}

	/**
	 * Sets the new X rotation of the transform
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setRotX(float x) {
		_rotation.x = x;
		setChanged();
	}

	/**
	 * Sets the new Y rotation of the transform
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setRotY(float y) {
		_rotation.y = y;
		setChanged();
	}

	/**
	 * Sets the new Z rotation of the transform
	 * 
	 * @param z
	 *            z-coordinate
	 */
	public void setRotZ(float z) {
		_rotation.z = z;
		setChanged();
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
		setChanged();
	}

	/**
	 * Gets the current transform scale
	 * 
	 * @return current scale
	 */
	public Vector3fc getScale() {
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
		setChanged();
	}

	/**
	 * Sets the new X scale of the transform
	 * 
	 * @param x
	 *            how much to scale on x axis
	 */
	public void setScaleX(float x) {
		_scale.x = x;
		setChanged();
	}

	/**
	 * Sets the new Y scale of the transform
	 * 
	 * @param y
	 *            how much to scale on y axis
	 */
	public void setScaleY(float y) {
		_scale.y = y;
		setChanged();
	}

	/**
	 * Sets the new Z scale of the transform
	 * 
	 * @param z
	 *            how much to scale on z axis
	 */
	public void setScaleZ(float z) {
		_scale.z = z;
		setChanged();
	}

	/**
	 * Updates the local world matrix if the transform has changed since the
	 * last time we retrieved it, and returns the new matrix
	 * 
	 * @return the matrix representing the current transform in world
	 *         coordinates
	 */
	public Matrix4fc getLocalToWorldMatrix() {
		// If the transform has changed since the last time this was called then
		// lets update the matrix
		if (_hasChanged) {

			// Set the world matrix of the updated transformation
			MatrixUtils.setWorldMatrix(_localToWorldMatrix, _position, _rotation, _scale);

			// Set the local to world matrix in relation to the parent if we
			// have one
			GameObject parent = getGameObject().getParent();
			if (parent != null)
				parent.getTransform().getLocalToWorldMatrix().mul(_localToWorldMatrix, _localToWorldMatrix);

			// Reset
			_hasChanged = false;
		}

		return _localToWorldMatrix;
	}

	/**
	 * Has this transform changed since the last time we got the world matrix
	 * 
	 * @return true if transform has changed before the last call to get the
	 *         world matrix
	 */
	public boolean hasChanged() {
		return _hasChanged;
	}

	/**
	 * For debugging purposes only
	 */
	@Override
	public String toString() {
		return String.format("Translation: %s, Rotation: %s, Scale: %s", _position, _rotation, _scale);
	}

	/**
	 * Sets this transform to "changed" and any of the gameobject's children to
	 * "changed". If the transform has changed, that means it must recalculate
	 * its world positions on its next matrix retrieval
	 */
	protected void setChanged() {
		// If we have already changed then we don't need to update the children
		// again
		if (_hasChanged)
			return;

		this._hasChanged = true;

		// Now update each child
		for (GameObject child : getGameObject().getChildren()) {
			child.getTransform().setChanged();
		}
	}

	/**
	 * The transform is required per game object, so we want to dispose of the
	 * game object if somebody tries to destroy its transform
	 */
	@Override
	protected boolean destroyGameObjectOnDispose() {
		return true;
	}
}
