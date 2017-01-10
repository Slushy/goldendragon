package engine.lighting;

import org.joml.Vector3f;

import engine.common.Defaults;
import engine.utils.math.MathUtils;

/**
 * Represents the base light source of every object
 * 
 * @author Brandon Porter
 *
 */
public final class AmbientLight {
	private boolean _isDirty = true;
	private float _intensity = Defaults.Lighting.LIGHT_INTENSITY;
	private final Vector3f _color = new Vector3f(Defaults.Lighting.LIGHT_COLOR);
	private final Vector3f _totalLight = new Vector3f();

	/**
	 * Constructs an ambient light
	 */
	protected AmbientLight() {
	}

	/**
	 * Sets the ambient light brightness - this is the additional light
	 * brightness that will be applied to every object in the scene
	 * 
	 * @param brightness
	 *            the brightness value between [0 - 1]
	 */
	public final void setBrightness(float brightness) {
		_intensity = MathUtils.clamp(brightness, 0, 1);
		_isDirty = true;
	}

	/**
	 * Sets the color for the ambient light - this is the additional light color
	 * applied to every object in the scene
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
		_isDirty = true;
	}

	/**
	 * Checks if the ambient light parameters have been changed since that last
	 * time we retrieved it. If we have it recalculates it before returning it
	 * 
	 * @return the current ambient light vector of the scene
	 */
	public final Vector3f getLight() {
		// If the params changed then we recalculate the ambient light
		if (_isDirty) {
			_totalLight.x = _color.x * _intensity;
			_totalLight.y = _color.y * _intensity;
			_totalLight.z = _color.z * _intensity;

			_isDirty = false;
		}

		return _totalLight;
	}
}
