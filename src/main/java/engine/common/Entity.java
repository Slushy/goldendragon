package engine.common;

/**
 * Base class for all gameObjects and components within a scene
 * 
 * @author Brandon Porter
 *
 */
public abstract class Entity {
	private String _name = "Entity";
	private boolean _isDisposed = false;
	
	public Entity(String name) {
		this._name = name;
	}
	/**
	 * @return name of the entity
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Sets the name of the entity
	 * 
	 * @param name
	 *            name to be referenced by within a lookup table
	 */
	public void setName(String name) {
		this._name = name;
	}
	
	/**
	 * @return true or false if the current entity has been disposed
	 */
	public boolean isDisposed() {
		return _isDisposed;
	}
	
	/**
	 * Destroys the entity and removes from scene.
	 */
	public void dispose() {
		onDispose();
		this._isDisposed = true;
	}
	
	/**
	 * To be implemented by inherited classes, called when it is time to destroy the entity
	 */
	protected abstract void onDispose();
}
