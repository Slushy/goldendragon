package engine.graphics;

import org.joml.Vector3f;

import engine.graphics.geometry.Texture;

/**
 * Abstract class to hold the name of a uniform for a specific material
 * property. This internal to the package and is only used by the
 * MaterialPropertyBlock
 * 
 * @author Brandon Porter
 *
 */
abstract class MaterialProperty {
	public final String uniformName;

	/*
	 * Constructs a new material property
	 */
	protected MaterialProperty(UniformType uniformType) {
		this.uniformName = uniformType.getName();
	}
	
	/**
	 * Material property to hold a texture value
	 * 
	 * @author Brandon Porter
	 *
	 */
	protected static class TextureProperty extends MaterialProperty {
		public Texture value = null;

		/**
		 * Constructs a new texture material property
		 * 
		 * @param uniformType
		 *            the type of uniform this property maps to
		 */
		public TextureProperty(UniformType uniformType) {
			super(uniformType);
		}

		/**
		 * Sets the texture property value to the passed in value. If the property
		 * reference is null, it creates it with the passed in uniform type,
		 * assigns the instance to the property reference, and sets the passed
		 * in value
		 * 
		 * @param propRef
		 * @param uniformType
		 * @param value
		 */
		public static TextureProperty set(TextureProperty propRef, UniformType uniformType, Texture value) {
			if (propRef == null)
				propRef = new TextureProperty(uniformType);

			propRef.value = value;
			return propRef;
		}
	}

	/**
	 * Material property to hold a vector3f value
	 * 
	 * @author Brandon Porter
	 *
	 */
	protected static class Vector3fProperty extends MaterialProperty {
		public final Vector3f value = new Vector3f();

		/**
		 * Constructs a new Vector3f material property
		 * 
		 * @param uniformType
		 *            the type of uniform this property maps to
		 */
		public Vector3fProperty(UniformType uniformType) {
			super(uniformType);
		}

		/**
		 * Sets the vector3f property value to the passed in x, y, z values. If
		 * the property reference is null, it creates it with the passed in
		 * uniform type, assigns the instance to the property reference, and
		 * sets the passed in values
		 * 
		 * @param propRef
		 * @param uniformType
		 * @param x
		 * @param y
		 * @param z
		 */
		public static Vector3fProperty set(Vector3fProperty propRef, UniformType uniformType, float x, float y, float z) {
			if (propRef == null)
				propRef = new Vector3fProperty(uniformType);

			propRef.value.set(x, y, z);
			return propRef;
		}
	}

	/**
	 * Material property to hold a vector3f value
	 * 
	 * @author Brandon Porter
	 *
	 */
	protected static class FloatProperty extends MaterialProperty {
		public float value = 0;

		/**
		 * Constructs a new float material property
		 * 
		 * @param uniformType
		 *            the type of uniform this property maps to
		 */
		public FloatProperty(UniformType uniformType) {
			super(uniformType);
		}

		/**
		 * Sets the float property value to the passed in value. If the property
		 * reference is null, it creates it with the passed in uniform type,
		 * assigns the instance to the property reference, and sets the passed
		 * in value
		 * 
		 * @param propRef
		 * @param uniformType
		 * @param value
		 */
		public static FloatProperty set(FloatProperty propRef, UniformType uniformType, float value) {
			if (propRef == null)
				propRef = new FloatProperty(uniformType);

			propRef.value = value;
			return propRef;
		}
	}
}
