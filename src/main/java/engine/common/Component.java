package engine.common;

import engine.scenes.Scene;

/**
 * Represents the base class for all components
 * 
 * @author Brandon Porter
 *
 */
public abstract class Component extends Entity {
	private GameObject _gameObject;

	/**
	 * Constructs a new component entity
	 * 
	 * @param componentName
	 *            name of the component
	 */
	public Component(String componentName) {
		super(componentName);
	}

	/**
	 * @return the game object owning this component
	 */
	public final GameObject getGameObject() {
		return _gameObject;
	}

	/**
	 * @return the transform of the game object owning this component
	 */
	public Transform getTransform() {
		return _gameObject != null ? _gameObject.getTransform() : null;
	}

	/**
	 * Stores a reference to the game object containing this component
	 * 
	 * @param gameObject
	 */
	protected void setGameObject(GameObject gameObject) {
		this._gameObject = gameObject;
	}

	/**
	 * @return the scene of the component
	 */
	protected Scene getScene() {
		return _gameObject != null ? _gameObject.getScene() : null;
	}

	/**
	 * @return true or false if this component should destroy the game object
	 *         when the component is disposed. This is only used for "required"
	 *         components. A required component should override this method and
	 *         return true, because when a required component is destroyed we
	 *         SHOULD also destroy the game object
	 */
	protected boolean destroyGameObjectOnDispose() {
		return false;
	}

	/**
	 * Disposes the component by removing itself from the game object
	 */
	@Override
	protected void onDispose() {
		if (_gameObject == null)
			return;

		// We have a valid game object so lets remove this component and check
		// if we should also dispose the game object
		_gameObject.removeComponent(this);
		if (destroyGameObjectOnDispose())
			_gameObject.dispose();
	}
}
