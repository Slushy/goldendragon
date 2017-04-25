package engine.graphics;

import engine.common.Entity;

/**
 * A material is representative of how a game object looks; e.g. with textures,
 * lighting, colors, etc.
 * 
 * @author Brandon Porter
 *
 */
public abstract class Material extends Entity {
	private final ShaderType _shaderType;

	/**
	 * Constructs a new material with with the specified entity name and shader
	 * type
	 * 
	 * @param entityName
	 *            default name of the entity
	 * @param shaderType
	 *            the type of shader to use when rendering this material
	 */
	public Material(String entityName, ShaderType shaderType) {
		super(entityName);
		this._shaderType = shaderType;
	}

	/**
	 * Copy Constructor to construct a new material from an existing one
	 * 
	 * @param material
	 *            material to copy
	 */
	public Material(Material material) {
		this(material.getName(), material.getShaderType());
	}

	/**
	 * @return the shader type to use for the material
	 */
	public ShaderType getShaderType() {
		return _shaderType;
	}

	/**
	 * Compares the material for equality against the current material
	 * 
	 * @param mat
	 *            the material to compare
	 * @return true if they are equal in instance or shader
	 */
	public boolean compare(Material mat) {
		if (this == mat)
			return true;

		// If the shader type are the same, then return true
		return _shaderType == mat.getShaderType();
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
}
