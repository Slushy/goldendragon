package engine.rendering.shader;

import engine.app.config.Defaults;

/**
 * A uniform represents a global shader variable. This is essentially only used
 * within this package, as it represents static "uniform" types when
 * registering shaders
 * 
 * @author Brandon Porter
 *
 */
public class UniformType {
		// ======================
		// Global
		// ======================
		public static final UniformType PROJECTION_MATRIX = new UniformType("projectionMatrix");
		
		// ======================
		// Material specific
		// ======================
		public static final UniformType MAIN_TEXTURE = new UniformType("mainTexture");
		public static final UniformType COLOR = new UniformType("color");
		public static final UniformType USE_TEXTURE = new UniformType("useTexture");
		public static final UniformType SHININESS = new UniformType("shininess");
		public static final UniformType SPECULAR_COLOR = new UniformType("specularColor");
		
		// ======================
		// Model specific
		// ======================
		public static final UniformType WORLD_VIEW_MATRIX = new UniformType("worldViewMatrix");
		
		// ======================
		// Lighting
		// ======================
		public static final UniformType AMBIENT_LIGHT = new UniformType("ambientLight");
		
		public static final UniformType DIRECTIONAL_LIGHT_COLOR = new UniformType("directionalLight.color");
		public static final UniformType DIRECTIONAL_LIGHT_INTENSITY = new UniformType("directionalLight.intensity");
		public static final UniformType DIRECTIONAL_LIGHT_DIRECTION = new UniformType("directionalLight.direction");
		
		public static final UniformType POINT_LIGHT_COLOR = new UniformType("pointLights[%d].color", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);
		public static final UniformType POINT_LIGHT_INTENSITY = new UniformType("pointLights[%d].intensity", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);
		public static final UniformType POINT_LIGHT_POSITION = new UniformType("pointLights[%d].position", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);
		public static final UniformType POINT_LIGHT_RANGE = new UniformType("pointLights[%d].range", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);
		// spotlight specific
		public static final UniformType POINT_LIGHT_DIRECTION = new UniformType("pointLights[%d].direction", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);
		public static final UniformType POINT_LIGHT_COS_HALF_ANGLE = new UniformType("pointLights[%d].cosHalfAngle", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);
		public static final UniformType POINT_LIGHT_IS_SPOT = new UniformType("pointLights[%d].isSpot", Defaults.Lighting.MAX_RENDERED_POINT_LIGHTS_PER_OBJECT);

		public static final UniformType ATTENUATION_CONSTANT = new UniformType("attenuation.constant");
		public static final UniformType ATTENUATION_QUADRATIC = new UniformType("attenuation.quadratic");
		
		private static final int NONE = 0;
		
		private final String _name;
		private final int _arrayCount;

		/**
		 * Constructs a normal uniform of the specified name
		 * 
		 * @param name
		 *            name to reference the uniform
		 */
		public UniformType(String name) {
			this(name, NONE);
		}

		/**
		 * Constructs an array of uniforms of the specified name
		 * 
		 * @param name
		 *            name to reference the uniform
		 * @param arrayCount
		 *            the number of uniforms this specified uniform array should
		 *            represent
		 */
		public UniformType(String name, int arrayCount) {
			this._name = name;
			this._arrayCount = arrayCount;
		}

		/**
		 * @return the reference name of this uniform
		 */
		public String getName() {
			return _name;
		}

		/**
		 * @return the number of uniforms this specified uniform array should
		 *         represent. If it's an array it will be > 0, else it will be one
		 *         uniform
		 */
		public int getCount() {
			return _arrayCount;
		}

		/**
		 * @return true if this uniform is an array
		 */
		public boolean isArray() {
			return getCount() > NONE;
		}
	}
