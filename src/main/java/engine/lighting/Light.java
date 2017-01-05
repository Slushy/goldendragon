package engine.lighting;

import engine.common.Component;

/**
 * Represents a light source in our scene
 * 
 * @author Brandon Porter
 *
 */
public abstract class Light extends Component {
	/**
	 * Represents the base color and brightness for every object in the scene
	 */
	public static final AmbientLight AMBIENT_LIGHT = new AmbientLight();

	/**
	 * Constructs a new light source
	 * 
	 * @param componentName
	 *            name of the light component
	 */
	public Light(String componentName) {
		super(componentName);
		// TODO Auto-generated constructor stub
	}

}
