package engine.common;

import java.util.ArrayList;
import java.util.List;

import engine.common.components.Transform;
import engine.scenes.Scene;

/**
 * A game object simply combines multiple components
 * 
 * @author Brandon Porter
 *
 */
public class GameObject extends Entity {
	private final List<Component> _components = new ArrayList<>();

	// Special components that can be referenced individually
	private final Transform _transform = new Transform();

	private Scene _scene = null;

	/**
	 * Constructs a new game object entity
	 */
	public GameObject() {
		this("GameObject");
	}

	/**
	 * Called when this game object is added to a scene
	 * 
	 * @param scene
	 *            the scene this object was added to
	 */
	public void addedToScene(Scene scene) {
		this._scene = scene;
	}

	/**
	 * @return the scene this game object belongs to
	 */
	public Scene getScene() {
		return _scene;
	}

	/**
	 * Constructs a new game object entity with the specified name
	 * 
	 * @param name
	 *            name to represent the game object
	 */
	public GameObject(String name) {
		super(name);
		// Every game object must have a transform
		this.addComponent(_transform);
	}

	/**
	 * Adds a new component to this game object
	 * 
	 * @param component
	 *            component to attach to this game object
	 */
	public void addComponent(Component component) {
		_components.add(component);
		component.setGameObject(this);
	}

	/**
	 * @return the list of attached components
	 */
	public final List<Component> getComponents() {
		return _components;
	}

	/**
	 * Tries to find the first component of the specified type, null if not
	 * found
	 * 
	 * @param componentClass
	 * @return component of specified type
	 */
	public <T extends Component> T getComponentByType(Class<T> componentClass) {
		for (Component component : getComponents()) {
			if (componentClass.isInstance(component))
				return (T) component;
		}
		return null;
	}

	/**
	 * 
	 * @param component
	 */
	protected void removeComponent(Component component) {
		_components.remove(component);
	}

	/**
	 * @return the transform of the game object
	 */
	public final Transform getTransform() {
		return _transform;
	}

	/**
	 * Disposes the game object by disposing each attached component
	 */
	@Override
	protected void onDispose() {
		for (Component component : _components) {
			// It's necessary to set the components game object to null
			// to let it know the game object is being disposed and it
			// does not need to remove itself
			component.setGameObject(null);
			component.dispose();
		}

		_components.clear();
	}
}
