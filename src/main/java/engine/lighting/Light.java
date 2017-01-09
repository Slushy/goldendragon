package engine.lighting;

import org.joml.Vector3f;

import engine.common.Component;
import engine.common.Defaults;

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

	// The color of the light
	private final Vector3f _color = new Vector3f(Defaults.Lighting.LIGHT_COLOR);
	// The brightness of the light
	private float _intensity = Defaults.Lighting.LIGHT_INTENSITY;

	/**
	 * Constructs a new light source
	 * 
	 * @param componentName
	 *            name of the light component
	 */
	public Light(String componentName) {
		super(componentName);
	}

	/**
	 * @return the brightness of this light
	 */
	public final float getBrightness() {
		return _intensity;
	}

	/**
	 * Sets the brightness for this light
	 * 
	 * @param intensity
	 *            the brightness of the light [0 - 1]
	 */
	public final void setBrightness(float intensity) {
		this._intensity = intensity;
	}

	/**
	 * @return the color of this light
	 */
	public final Vector3f getColor() {
		return _color;
	}

	/**
	 * Sets the color for this light
	 * 
	 * @param r
	 *            RED-value [0-1]
	 * @param g
	 *            GREEN-value [0-1]
	 * @param b
	 *            BLUE-value [0-1]
	 */
	public final void setColor(float r, float g, float b) {
		_color.set(r, g, b);
	}
}
