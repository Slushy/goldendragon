package engine.common;

/**
 * Represents the base class for all components
 * 
 * @author Brandon Porter
 *
 */
public abstract class Component extends Entity {
	public static final int UPDATE = 1;
	public static final int RENDER = 2;

	private final int _capabilities;

	private GameObject _gameObject;

	/**
	 * Constructs a new component entity
	 * 
	 * @param componentName
	 *            name of the component
	 */
	public Component(String componentName) {
		super(componentName);
		// Sets the capabilities of this component
		this._capabilities = loadCapabilities();
	}

	/**
	 * @return the capabilities this component can support
	 */
	public int getCapabilities() {
		return _capabilities;
	}

	/**
	 * @return the game object owning this component
	 */
	public GameObject getGameObject() {
		return _gameObject;
	}

	/**
	 * The default a component can support is nothing
	 * 
	 * @return the capabilities this component can support; should be overridden
	 */
	protected int loadCapabilities() {
		return 0;
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
	 * Disposes the component by removing itself from the game object
	 */
	@Override
	protected void onDispose() {
		if (_gameObject != null)
			_gameObject.removeComponent(this);
	}
}
