package engine.lighting;

import engine.common.Defaults;

/**
 * Attenuation is a function of the distance and light - the intensity
 * (brightness) of light is inversely proportional to the square of distance.
 * This class represents the 3 variables that make up the attenuation equation
 * and can be used to adjust the attenuation per light source.
 * 
 * This class will allow us to determine the brightness of a pixel on a game
 * object based on the distance it is from a light source that has diminishing
 * light intensity over distance (i.e. ambient/directional lights won't be
 * affected).
 * 
 * @author Brandon Porter
 *
 */
public final class Attenuation {

	private float _constant = Defaults.Lighting.ATTENUATION_CONSTANT;
	private float _quadratic = Defaults.Lighting.ATTENUATION_QUADRATIC;

	/**
	 * Constructs a new attenuation model, protected to keep instantiation done
	 * within the lights package
	 */
	protected Attenuation() {
	}

	/**
	 * The constant is represented as the variable 'a' in the following
	 * equation:
	 * 
	 * att(a, b) = 1 / (a + b*r*r)
	 * 
	 * @return the constant associated with this attenuation equation
	 */
	public float getConstant() {
		return _constant;
	}

	/**
	 * Sets the constant that is represented as the variable 'a' in the
	 * following equation:
	 * 
	 * att(a, b) = 1 / (a + b*r*r)
	 * 
	 * @param constant
	 */
	public void setConstant(float constant) {
		this._constant = constant;
	}

	/**
	 * The quadratic constant is represented as the variable 'b' in the
	 * following equation:
	 * 
	 * att(a, b) = 1 / (a + b*r*r)
	 * 
	 * @return the quadratic associated with this attenuation equation
	 */
	public float getQuadratic() {
		return _quadratic;
	}

	/**
	 * Sets the quadratic constant that is represented as the variable 'b' in
	 * the following equation:
	 * 
	 * att(a, b) = 1 / (a + b*r*r)
	 * 
	 * @param quadraticConstant
	 */
	public void setQuadratic(float quadraticConstant) {
		this._quadratic = quadraticConstant;
	}
}
