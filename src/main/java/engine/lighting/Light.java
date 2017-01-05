package engine.lighting;

import org.joml.Vector3f;

import engine.common.Component;
import engine.common.Defaults;
import engine.utils.math.MathUtils;

public abstract class Light extends Component {
	private static boolean _ambientLightDirty = true;
	private static float _ambientLightIntensity = Defaults.Lighting.LIGHT_INTENSITY;
	private static final Vector3f _ambientLightColor = new Vector3f(Defaults.Lighting.LIGHT_COLOR);
	private static Vector3f _ambientLight = new Vector3f();

	public Light(String componentName) {
		super(componentName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets the ambient light brightness - this is the additional light
	 * brightness that will be applied to every object in the scene
	 * 
	 * @param brightness
	 *            the brightness value between [0 - 1]
	 */
	public static final void setAmbientLightBrightness(float brightness) {
		_ambientLightIntensity = MathUtils.clamp(brightness, 0, 1);
		_ambientLightDirty = true;
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
	public static final void setAmbientLightColor(float r, float g, float b) {
		_ambientLightColor.set(r, g, b);
		_ambientLightDirty = true;
	}

	/**
	 * Checks if the ambient light parameters have been changed since that last
	 * time we retrieved it. If we have it recalculates it before returning it
	 * 
	 * @return the current ambient light vector of the scene
	 */
	public static final Vector3f getAmbientLight() {
		// If the params changed then we recalculate the ambient light
		if (_ambientLightDirty) {
			_ambientLight.x = _ambientLightColor.x * _ambientLightIntensity;
			_ambientLight.y = _ambientLightColor.y * _ambientLightIntensity;
			_ambientLight.z = _ambientLightColor.z * _ambientLightIntensity;

			_ambientLightDirty = false;
		}

		return _ambientLight;
	}
}
