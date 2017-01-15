package engine.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import engine.guis.RectTransform;
import engine.scenes.Scene;

/**
 * A game object simply combines multiple components
 * 
 * @author Brandon Porter
 *
 */
public class GameObject extends Entity {
	private final List<Component> _components = new ArrayList<>();

	// Every transform is actually a rect transform secretly, but the default
	// type return for getTransform() is still Transform. At any time this
	// can be cast to a rect transform, but changing any of the extra properties
	// added in the rect transform is only useful for GUI components.
	private final Transform _transform = new RectTransform();
	private final List<GameObject> _children = new ArrayList<>();

	private Scene _scene = null;
	private Consumer<Component> _onAddedComponentCallback;
	private GameObject _parent = null;

	/**
	 * Constructs a new game object entity
	 */
	public GameObject() {
		this("GameObject");
	}

	/**
	 * @return the parent game object of the current game object, null if it has
	 *         no parent
	 */
	public GameObject getParent() {
		return _parent;
	}

	/**
	 * @return All children directly under this game object, if none it will
	 *         return an empty list
	 */
	public List<GameObject> getChildren() {
		return _children;
	}

	/**
	 * Sets the parent game object of the current game object, also if this game
	 * object already had an existing parent then it will no longer be a child
	 * of that game object
	 * 
	 * @param parent
	 *            the parent game object
	 */
	public void setParent(GameObject parent) {
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
		_transform.setChanged();
	}

	/**
	 * Removes the game object from its children. This game object is no longer
	 * the child's parent.
	 * 
	 * @param child
	 *            game object to remove from children
	 */
	public void removeChild(GameObject child) {
		_children.remove(child);
	}

	/**
	 * Adds the game object to its children. This game object is now the child's
	 * parent.
	 * 
	 * @param child
	 *            game object to add to children
	 */
	public void addChild(GameObject child) {
		_children.add(child);
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
		// TODO: Throw exception later if duplicate component exists
		_components.add(component);
		component.setGameObject(this);
		// Pass the new component to the scene to register it
		if (_onAddedComponentCallback != null)
			_onAddedComponentCallback.accept(component);
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
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentByType(Class<T> componentClass) {
		for (Component component : getComponents()) {
			if (componentClass.isInstance(component))
				return (T) component;
		}
		return null;
	}

	/**
	 * Called when this game object is added to a scene
	 * 
	 * @param scene
	 *            the scene this object was added to
	 * @param onAddedComponentCallback
	 *            this callback is called every time a new component is added to
	 *            this game object during the active scene.
	 */
	public void addedToScene(Scene scene, Consumer<Component> onAddedComponentCallback) {
		this._scene = scene;
		this._onAddedComponentCallback = onAddedComponentCallback;
	}

	/**
	 * @return the scene this game object belongs to
	 */
	public Scene getScene() {
		return _scene;
	}

	/**
	 * @return the transform of the game object
	 */
	public final Transform getTransform() {
		return _transform;
	}

	/**
	 * Removes the component from this game object
	 * 
	 * @param component
	 *            the component to remove
	 */
	protected void removeComponent(Component component) {
		_components.remove(component);
	}

	/**
	 * Disposes the game object by disposing each attached component
	 */
	@Override
	protected void onDispose() {
		// Remove itself from its parent
		if (_parent != null) {
			_parent.removeChild(this);
			_parent = null;
		}
		
		// Dispose each child
		for (GameObject child : _children) {
			child._parent = null; // We don't want the child to remove itself
									// from our children as we are looping over
									// it or set the transform to changed
			child.dispose();
		}
		_children.clear();

		// Dispose each component
		for (Component component : _components) {
			// It's necessary to set the components game object to null
			// to let it know the game object is being disposed and it
			// does not need to remove itself
			component.setGameObject(null);
			component.dispose();
		}
		_components.clear();

		this._scene = null;
		this._onAddedComponentCallback = null;
	}
}
