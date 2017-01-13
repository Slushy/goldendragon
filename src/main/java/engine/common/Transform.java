package engine.common;

import java.util.ArrayList;
import java.util.List;

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
	private final List<Transform> _children = new ArrayList<>();

	private boolean _isDirty = false;
	private Transform _parent = null;

	/**
	 * Constructs a transform component
	 */
	public Transform() {
		super(COMPONENT_NAME);
	}

	/**
	 * @return the parent transform of the current transform, null this
	 *         transform has no parent
	 */
	public Transform getParent() {
		return _parent;
	}

	/**
	 * Sets the parent transform of the current transform, also if this
	 * transform already had an existing parent then it will no longer be a
	 * child of that transform
	 * 
	 * @param parent
	 *            the parent transform
	 */
	public void setParent(Transform parent) {
		// Remove itself from existing parent
		if (_parent != null)
			_parent.removeChild(this);

		// Set the parent
		this._parent = parent;

		// Add itself to new parent
		if (parent != null)
			parent.addChild(this);

		// We have changed parents, so our transformation properties most likely
		// need to be changed too
		this.setDirty();
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
		setDirty();
	}

	/**
	 * Sets the new X position of the transform
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setPosX(float x) {
		_position.x = x;
		setDirty();
	}

	/**
	 * Sets the new Y position of the transform
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setPosY(float y) {
		_position.y = y;
		setDirty();
	}

	/**
	 * Sets the new Z position of the transform
	 * 
	 * @param z
	 *            z-coordinate
	 */
	public void setPosZ(float z) {
		_position.z = z;
		setDirty();
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
		setDirty();
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
		setDirty();
	}

	/**
	 * Sets the new X rotation of the transform
	 * 
	 * @param x
	 *            x-coordinate
	 */
	public void setRotX(float x) {
		_rotation.x = x;
		setDirty();
	}

	/**
	 * Sets the new Y rotation of the transform
	 * 
	 * @param y
	 *            y-coordinate
	 */
	public void setRotY(float y) {
		_rotation.y = y;
		setDirty();
	}

	/**
	 * Sets the new Z rotation of the transform
	 * 
	 * @param z
	 *            z-coordinate
	 */
	public void setRotZ(float z) {
		_rotation.z = z;
		setDirty();
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
		setDirty();
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
		setDirty();
	}

	/**
	 * Sets the new X scale of the transform
	 * 
	 * @param x
	 *            how much to scale on x axis
	 */
	public void setScaleX(float x) {
		_scale.x = x;
		setDirty();
	}

	/**
	 * Sets the new Y scale of the transform
	 * 
	 * @param y
	 *            how much to scale on y axis
	 */
	public void setScaleY(float y) {
		_scale.y = y;
		setDirty();
	}

	/**
	 * Sets the new Z scale of the transform
	 * 
	 * @param z
	 *            how much to scale on z axis
	 */
	public void setScaleZ(float z) {
		_scale.z = z;
		setDirty();
	}

	/**
	 * Updates the local world matrix if the transform has changed since the
	 * last time we retrieved it, and returns the new matrix
	 * 
	 * @return the matrix representing the current transform in world
	 *         coordinates
	 */
	public Matrix4fc getLocalToWorldMatrix() {
		// If the transform is dirty (if it has changed any of it or its
		// parent's transformation properties) then lets update the matrix
		if (_isDirty) {

			// Set the world matrix of the updated transformation
			MatrixUtils.setWorldMatrix(_localToWorldMatrix, _position, _rotation, _scale);

			// Set the local to world matrix in relation to the parent if we
			// have one
			if (_parent != null)
				_parent.getLocalToWorldMatrix().mul(_localToWorldMatrix, _localToWorldMatrix);

			// Reset
			_isDirty = false;
		}

		return _localToWorldMatrix;
	}

	/**
	 * For debugging purposes only
	 */
	@Override
	public String toString() {
		return String.format("Translation: %s, Rotation: %s, Scale: %s", _position, _rotation, _scale);
	}

	/**
	 * The transform is required per game object, so we want to dispose of the
	 * game object if somebody tries to destroy its transform
	 */
	@Override
	protected boolean destroyGameObjectOnDispose() {
		return true;
	}

	/**
	 * Called from the base Entity class when it is being disposed
	 */
	@Override
	protected void onDispose() {
		// Remove itself from its parent
		if (_parent != null)
			_parent.removeChild(this);

		// Dispose each child
		for (Transform child : _children) {
			child._parent = null; // yep
			child.dispose();
		}
		_children.clear();
		
		// Let the parent destroy the game object (since a transform is required)
		super.onDispose();
	}

	/**
	 * Removes the transform from its children. This transform is no longer the
	 * child's parent.
	 * 
	 * @param child
	 *            transform to remove from children
	 */
	private void removeChild(Transform child) {
		_children.remove(child);
	}

	/**
	 * Adds the transform to its children. This transform is now the child's
	 * parent.
	 * 
	 * @param child
	 *            transform to add to children
	 */
	private void addChild(Transform child) {
		_children.add(child);
	}

	/**
	 * Sets this transform to dirty and any of its children to dirty. If the
	 * transform is "dirty", that means it must recalculate its world positions
	 */
	private void setDirty() {
		// If we are already dirty we don't need to set it or its children
		if (_isDirty)
			return;

		_isDirty = true;

		// Now update each child
		for (Transform child : _children) {
			child.setDirty();
		}
	}
}
