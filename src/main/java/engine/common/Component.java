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
	 * Disposes the component by removing itself from the game object
	 */
	@Override
	protected void onDispose() {
		if (_gameObject != null)
			_gameObject.removeComponent(this);
	}
}
