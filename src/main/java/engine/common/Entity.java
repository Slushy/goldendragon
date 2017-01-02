package engine.common;

/**
 * Base class for all gameObjects and components within a scene
 * 
 * @author Brandon Porter
 *
 */
public abstract class Entity {
	private static long TOTAL_ENTITIES = 0;
	
	private final long _instanceId = TOTAL_ENTITIES++;
	
	private String _name = "Entity";
	private boolean _isDisposed = false;
	
	public Entity(String name) {
		this._name = name;
	}
	
	/**
	 * @return name of the entity
	 */
	public final String getName() {
		return _name;
	}
	
	/**
	 * @return unique instanceID of this entity
	 */
	public final long getInstanceId() {
		return _instanceId;
	}

	/**
	 * Sets the name of the entity
	 * 
	 * @param name
	 *            name to be referenced by within a lookup table
	 */
	public final void setName(String name) {
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
	protected void onDispose() {}
}
