package engine.lighting;

import engine.utils.Debug;

/**
 * A directional light represents a distant light source which exists infinitely
 * far away. All objects in the scene are illuminated as if the light is always
 * from the same direction. A directional light does not diminish over distance.
 * A great example of a directional light is the sun.
 * 
 * @author Brandon Porter
 *
 */
public class DirectionalLight extends Light {
	public static final int Z_POSITION = -1;
	
	/**
	 * Creats a new directional light component
	 */
	public DirectionalLight() {
		super("Directional Light");
	}

	// Called once when the scene becomes active
	@SuppressWarnings("unused")
	private void start() {
		Debug.log("Directional light setting itself as sun");
		this.getScene().getRenderer().setDirectionalLight(this);
	}
}
