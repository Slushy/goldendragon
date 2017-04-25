package engine.graphics;

/**
 * Holds all of the uniform types that each shader type can have
 * 
 * @author Brandon Porter
 *
 */
public abstract class ShaderUniforms {
	protected abstract UniformType[] getUniforms();

	/**
	 * Holds all of the uniform types for the Standard Shader
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Standard extends ShaderUniforms {
		private final UniformType[] UNIFORMS = { UniformType.MAIN_TEXTURE, UniformType.COLOR, UniformType.USE_TEXTURE,
				UniformType.PROJECTION_MATRIX, UniformType.WORLD_VIEW_MATRIX, UniformType.AMBIENT_LIGHT,
				UniformType.DIRECTIONAL_LIGHT_COLOR, UniformType.DIRECTIONAL_LIGHT_INTENSITY,
				UniformType.DIRECTIONAL_LIGHT_DIRECTION, UniformType.POINT_LIGHT_COLOR,
				UniformType.POINT_LIGHT_INTENSITY, UniformType.POINT_LIGHT_POSITION, UniformType.POINT_LIGHT_RANGE,
				UniformType.POINT_LIGHT_DIRECTION, UniformType.POINT_LIGHT_COS_HALF_ANGLE,
				UniformType.POINT_LIGHT_IS_SPOT, UniformType.ATTENUATION_CONSTANT, UniformType.ATTENUATION_QUADRATIC,
				UniformType.SHININESS, UniformType.SPECULAR_COLOR };

		/**
		 * Constructs a standard shader uniform wrapper
		 */
		protected Standard() {
		}

		@Override
		protected UniformType[] getUniforms() {
			return UNIFORMS;
		}
	}

	/**
	 * Holds all of the uniform types for the GUI Shader
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Gui extends ShaderUniforms {
		private final UniformType[] UNIFORMS = { UniformType.MAIN_TEXTURE, UniformType.COLOR, UniformType.USE_TEXTURE,
				UniformType.PROJECTION_MATRIX, UniformType.WORLD_VIEW_MATRIX };

		/**
		 * Constructs a gui shader uniform wrapper
		 */
		protected Gui() {
		}

		@Override
		protected UniformType[] getUniforms() {
			return UNIFORMS;
		}
	}
}
