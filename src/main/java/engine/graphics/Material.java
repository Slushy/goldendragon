package engine.graphics;

import engine.common.Defaults;
import engine.common.Entity;
import engine.graphics.geometry.Texture;

/**
 * A material is representative of how a game object is displayed and rendered
 * with the specified shader
 * 
 * @author Brandon
 *
 */
public class Material extends Entity {
	public static final Material DEFAULT = new Material();
	private static final String ENTITY_NAME = "Material";

	private final MaterialPropertyBlock _properties = new MaterialPropertyBlock();

	private ShaderType _shaderType = ShaderType.STANDARD;

	/**
	 * Copy Constructor to construct a new material from an existing one
	 * 
	 * @param material
	 *            material to clone
	 */
	public Material(Material material) {
		super(material.getName());
		this._shaderType = material.getShaderType();
		this._properties.cloneFrom(material.getProperties());
	}

	/**
	 * Constructs a new material with the main texture
	 * 
	 * @param mainTexture
	 *            the main texture to render the mesh using this material
	 */
	public Material(Texture mainTexture) {
		this();
		_properties.setMainTexture(mainTexture);
	}

	/**
	 * Constructs a new material with the specified shader type
	 * 
	 * @param shaderType
	 *            the type of shader to use when rendering this material
	 */
	public Material(ShaderType shaderType) {
		this();
		this._shaderType = shaderType;
	}

	/**
	 * Constructs a new material with no texture, default color and the standard
	 * shader
	 */
	public Material() {
		super(ENTITY_NAME);
		this.setDefaults();
	}

	/**
	 * @return the properties being used to render this material
	 */
	public final MaterialPropertyBlock getProperties() {
		return _properties;
	}

	/**
	 * @return the shader type to use for the material
	 */
	public ShaderType getShaderType() {
		return _shaderType;
	}

	/**
	 * @return true if the mesh has a texture, false otherwise
	 */
	public boolean hasTexture() {
		return _properties.getMainTexture() != null;
	}

	/**
	 * Compares the material for equality against the current material
	 * 
	 * @param mat
	 *            the material to compare
	 * @return true if they are equal in instance or properties
	 */
	public boolean compare(Material mat) {
		if (this == mat)
			return true;

		// If both the properties and shader type are the same, then return true
		return _properties.compare(mat.getProperties()) && _shaderType == mat.getShaderType();
	}

	/**
	 * Cleans up the material
	 */
	@Override
	protected void onDispose() {
		// TODO: Remove this because texture is probably
		// a shared element, so it should dispose itself
		// if (hasTexture())
		// _texture.dispose();
	}

	/*
	 * Initializes this material with default property values
	 */
	private void setDefaults() {
		_properties.setColor(Defaults.Materials.COLOR);
		_properties.setSpecularColor(Defaults.Materials.SPECULAR_COLOR);
		_properties.setShininess(Defaults.Materials.SHININESS_MIN);
	}
}
