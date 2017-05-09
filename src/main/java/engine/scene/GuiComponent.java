package engine.scene;

import engine.rendering.GuiRenderer;

/**
 * A GUI component is another type of component that can be added to a game
 * object that deals with other GUI components. These will be used for 2D
 * transformations.
 * 
 * @author Brandon Porter
 *
 */
public abstract class GuiComponent extends Component {

	/**
	 * Constructs a new GUI component with the specified name
	 * 
	 * @param componentName
	 *            name of the component
	 */
	public GuiComponent(String componentName) {
		super(componentName);
	}

	/**
	 * Gets the transform of the component as a RectTransform
	 */
	@Override
	public RectTransform getTransform() {
		return (RectTransform) super.getTransform();
	}
	
	/**
	 * Sets the component's current game object, and also adds a new GUI
	 * renderer if one doesn't currently exist on the game object
	 */
	@Override
	protected final void setGameObject(GameObject gameObject) {
		super.setGameObject(gameObject);

		/**
		 * If a game object has any GUI component, it also needs a GUI renderer
		 */
		if (gameObject.getComponentByType(GuiRenderer.class) == null) {
			gameObject.addComponent(new GuiRenderer());
		}
	}
}
