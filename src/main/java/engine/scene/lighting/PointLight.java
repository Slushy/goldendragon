package engine.scene.lighting;

import engine.system.Defaults;

/**
 * A point light represents a light source that sends out light in all
 * directions uniformly. It has a set intensity (brightness) which diminishes
 * over distance from the light source. Thus point lights usually only affect
 * game objects in the nearby area of the point light, and far away enough
 * objects will not be affected.
 * 
 * @author Brandon Porter
 *
 */
public class PointLight extends Light {
	/**
	 * Holds the constants that represent the equation for retrieving the
	 * intensity of light at a certain distance from the center of the light
	 * source for all point lights
	 */
	public static final Attenuation ATTENUATION = new Attenuation();

	private static final String COMPONENT_NAME = "Point Light";

	private float _range = Defaults.Lighting.POINT_LIGHT_RANGE;

	/**
	 * Constructs a new point light component of default range
	 */
	public PointLight() {
		this(COMPONENT_NAME);
	}

	/**
	 * Constructs a new point light component with the specified intensity range
	 * 
	 * @param range
	 *            the point light's intensity range (also known as the distance
	 *            traveled from the middle of the light source before being
	 *            fully diminished)
	 */
	public PointLight(float range) {
		this();
		this.setRange(range);
	}

	/**
	 * Constructs a new point light (from inheriting class) with the specified
	 * component name
	 * 
	 * @param componentName
	 *            name of the component inheriting this class
	 */
	protected PointLight(String componentName) {
		super(componentName);
	}

	/**
	 * @return the point light's intensity range (also known as the distance
	 *         traveled from the middle of the light source before being fully
	 *         diminished)
	 */
	public float getRange() {
		return _range;
	}

	/**
	 * Sets this point light's intensity range [0 - INFINITY]
	 * 
	 * @param range
	 *            the point light's intensity range (also known as the distance
	 *            traveled from the middle of the light source before being
	 *            fully diminished)
	 */
	public void setRange(float range) {
		this._range = Math.max(0, range);
	}
}
