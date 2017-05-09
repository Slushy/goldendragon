package engine.scene.lighting;

import engine.app.config.Defaults;
import engine.utils.math.MathUtils;

/**
 * A spot light is a type of point light except that instead of sending out a
 * light in all directions uniformly, it sends it out in a cone shape (think
 * flash light) in the direction the light is facing.
 * 
 * @author Brandon Porter
 *
 */
public class SpotLight extends PointLight {
	private static final String COMPONENT_NAME = "Spot Light";

	private float _spotAngle;
	private float _cosHalfAngle;

	/**
	 * Constructs a new spot light with default range and spotlight angle
	 */
	public SpotLight() {
		this(Defaults.Lighting.SPOTLIGHT_ANGLE);
	}

	/**
	 * Constructs a new spotlight with default range and the specified angle
	 * 
	 * @param spotAngle
	 *            the spot light's cone cutoff angle
	 */
	public SpotLight(float spotAngle) {
		super(COMPONENT_NAME);
		this.setSpotAngle(spotAngle);
	}

	/**
	 * Constructs a new spotlight with specified range and angle
	 * 
	 * @param spotAngle
	 *            the spot light's cone cutoff angle
	 * @param range
	 *            the point light's intensity range (also known as the distance
	 *            traveled from the middle of the light source before being
	 *            fully diminished)
	 */
	public SpotLight(float spotAngle, float range) {
		this(spotAngle);
		this.setRange(range);
	}

	/**
	 * @return the spot light's cone cutoff angle
	 */
	public float getSpotAngle() {
		return _spotAngle;
	}

	/**
	 * Sets this spot lights cone cutoff angle [0 - 360]
	 * 
	 * @param spotAngle
	 *            the spot light's cone cutoff angle
	 */
	public void setSpotAngle(float spotAngle) {
		this._spotAngle = MathUtils.clamp(spotAngle, 0.0f, 360.0f);
		this._cosHalfAngle = (float) Math.cos(Math.toRadians(_spotAngle / 2.0f));
	}

	/**
	 * The cos half angle is exactly that: it is the Math.cos(SpotAngle / 2)
	 * which is updated every time the spot angle changes. It represents a float
	 * cutoff in the range [-1, 1] which we can use to compare against the the
	 * dot product of the spotlight & a point in the world to determine if that
	 * point is in the line of sight of the spotlight.
	 * 
	 * @return the value of Math.cos(SpotAngle / 2)
	 */
	public final float getCosHalfAngle() {
		return _cosHalfAngle;
	}
}
