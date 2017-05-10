package engine.rendering.material;

import org.joml.Vector3fc;

import engine.assets.Texture;
import engine.rendering.shader.ShaderType;
import engine.system.Defaults;

/**
 * A material is representative of how a game object looks; e.g. with textures,
 * lighting, colors, etc.
 * 
 * @author Brandon Porter
 *
 */
public class MeshMaterial extends Material {
	public static final MeshMaterial DEFAULT = new MeshMaterial();
	private static final String ENTITY_NAME = "Mesh Material";
	
	private final MaterialPropertyBlock _properties = new MaterialPropertyBlock();

	/**
	 * Constructs a new material with the specified shader type
	 * 
	 * @param shaderType
	 *            the type of shader to use when rendering this material
	 */
	public MeshMaterial(ShaderType shaderType) {
		super(ENTITY_NAME, shaderType);
		setDefaults();
	}
	
	/**
	 * Copy Constructor to construct a new material from an existing one
	 * 
	 * @param material
	 *            material to copy
	 */
	public MeshMaterial(MeshMaterial material) {
		super(material);
		this._properties.cloneFrom(material.getProperties());
	}

	/**
	 * Constructs a new material with no texture, default color and the standard
	 * shader
	 */
	public MeshMaterial() {
		this(ShaderType.STANDARD);
	}

	/**
	 * Constructs a new material with the main texture
	 * 
	 * @param mainTexture
	 *            the main texture to render the mesh using this material
	 */
	public MeshMaterial(Texture mainTexture) {
		this();
		_properties.setMainTexture(mainTexture);
	}
	
	/**
	 * Constructs a new material with a new color
	 * 
	 * @param color
	 *            the color to render the mesh using this material
	 */
	public MeshMaterial(Vector3fc color) {
		this();
		_properties.setColor(color);
	}


	/**
	 * @return the properties being used to render this material
	 */
	public final MaterialPropertyBlock getProperties() {
		return _properties;
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
	@Override
	public boolean compare(Material mat) {
		boolean areEqual = super.compare(mat) && mat instanceof MeshMaterial;
		return areEqual && _properties.compare(((MeshMaterial)mat).getProperties());
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
