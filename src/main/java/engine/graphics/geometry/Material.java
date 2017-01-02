package engine.graphics.geometry;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.common.Entity;
import engine.graphics.ShaderType;
import engine.graphics.StandardShaderProgram;

/**
 * A material is representative of how a game object looks; e.g. with textures,
 * lighting, colors, etc.
 * 
 * @author Brandon
 *
 */
public class Material extends Entity {
	public static final Material DEFAULT = new Material();
	private ShaderType _shaderType = ShaderType.STANDARD;

	private Vector3f _color = MaterialDefaults.COLOR;
	private Texture _texture = null;

	/**
	 * Constructs a new material with a default color
	 */
	public Material() {
		super("Material");
	}

	/**
	 * Copy Constructor to construct a new material from an existing one
	 * 
	 * @param material
	 *            material to copy
	 */
	public Material(Material material) {
		this();
		this.setColor(material.getColor());
		this.setTexture(material.getTexture());
	}

	/**
	 * Constructs a new textured material
	 * 
	 * @param texture
	 *            texture to initialize material with
	 */
	public Material(Texture texture) {
		this();
		this._texture = texture;
	}

	public final void renderStart(StandardShaderProgram shaderProgram) {
		shaderProgram.setColor(getColor());
		if (hasTexture()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture().getTextureId());
		}
	}

	public final void renderEnd() {
		if (hasTexture()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
	}

	/**
	 * @return the shader type to use for the material
	 */
	public ShaderType getShaderType() {
		return _shaderType;
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
	@Override
	protected void onDispose() {
		if (_texture != null)
			_texture.dispose();
		super.onDispose();
	}

	public static class MaterialDefaults {
		/**
		 * The default color of a non-textured material is White (255, 255, 255)
		 */
		public static final Vector3f COLOR = new Vector3f(1.0f, 1.0f, 1.0f);
	}
}
