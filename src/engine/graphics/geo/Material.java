package engine.graphics.geo;

import org.joml.Vector3f;

/**
 * A material is representative of how a game object looks; e.g. with textures,
 * lighting, colors, etc.
 * 
 * @author Brandon
 *
 */
public class Material {
	private Vector3f _color = null;
	private Texture _texture = null;

	/**
	 * Constructs a new material with a default color
	 */
	public Material() {
		this(MaterialDefaults.COLOR);
	}

	/**
	 * Constructs a new material with a color
	 * 
	 * @param color
	 *            color for the material
	 */
	public Material(Vector3f color) {
		this._color = color;
	}

	/**
	 * Constructs a new material with a texture
	 * 
	 * @param texture
	 *            texture for the material
	 */
	public Material(Texture texture) {
		this._texture = texture;
	}

	/**
	 * Copy Constructor to construct a new material from an existing one
	 * 
	 * @param material
	 *            material to copy
	 */
	public Material(Material material) {
		this.setColor(material.getColor());
		this.setTexture(material.getTexture());
	}

	/**
	 * @return the color that represents this material as a 3D vector
	 */
	public Vector3f getColor() {
		return _color;
	}

	/**
	 * Sets the color of this material
	 * 
	 * @param color
	 */
	public void setColor(Vector3f color) {
		this._color = color;
	}

	/**
	 * Sets the texture of the mesh
	 * 
	 * @param texture
	 *            texture to display on mesh
	 */
	public void setTexture(Texture texture) {
		this._texture = texture;
	}

	/**
	 * First disposes any existing texture before setting the new one
	 * 
	 * @param texture
	 *            texture to display on mesh, or null to clear
	 */
	public void replaceTexture(Texture texture) {
		if (hasTexture())
			_texture.dispose();
		setTexture(texture);
	}

	/**
	 * @return texture of the mesh
	 */
	public Texture getTexture() {
		return _texture;
	}

	/**
	 * @return true if the mesh has a texture, false otherwise
	 */
	public boolean hasTexture() {
		return _texture != null;
	}

	/**
	 * Cleans up the material
	 */
	public void dispose() {
		if (_texture != null)
			_texture.dispose();
	}

	public static class MaterialDefaults {
		/**
		 * The default color of a non-textured material is White (255, 255, 255)
		 */
		public static final Vector3f COLOR = new Vector3f(1.0f, 1.0f, 1.0f);
	}
}
