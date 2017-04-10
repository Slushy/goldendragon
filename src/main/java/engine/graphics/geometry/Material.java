package engine.graphics.geometry;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.common.Defaults;
import engine.common.Entity;
import engine.graphics.ShaderType;
import engine.graphics.StandardShaderProgram;
import engine.utils.math.MathUtils;

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
	private Texture _texture = null;
	private Vector3f _color = new Vector3f(Defaults.Materials.COLOR);
	private Vector3f _specularColor = new Vector3f(Defaults.Materials.SPECULAR_COLOR);
	private float _shininess = Defaults.Materials.SHININESS_MIN;

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
		this.setColor(material.getColor().x, material.getColor().y, material.getColor().z);
		this.setTexture(material.getTexture());
		this.setSpecularColor(material.getSpecularColor().x, material.getSpecularColor().y,
				material.getSpecularColor().z);
		this.setShininess(material.getShininess());
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

	/**
	 * Constructs a new colored material
	 * 
	 * @param color
	 *            color of the material
	 */
	public Material(Vector3f color) {
		this();
		this.setColor(color.x, color.y, color.z);
	}

	/**
	 * TEMPORARY
	 * 
	 * @param shaderProgram
	 */
	public final void renderStart(StandardShaderProgram shaderProgram) {
		shaderProgram.setColor(getColor());

		// Sets whether or not to use a texture
		boolean hasTexture = hasTexture();
		shaderProgram.useTexture(hasTexture);

		if (hasTexture) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture().getTextureId());
		}
	}

	/**
	 * TEMPORARY
	 */
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
	 * @param r
	 *            RED-value [0-1]
	 * @param g
	 *            GREEN-value [0-1]
	 * @param b
	 *            BLUE-value [0-1]
	 */
	public void setColor(float r, float g, float b) {
		this._color.set(r, g, b);
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
	 * @return The shininess factor of the material [0.01 - 1.0]
	 */
	public float getShininess() {
		return _shininess;
	}

	/**
	 * Sets the shininess factor of this material [0.01 - 1.0]
	 * 
	 * @param shininess
	 *            the closer to 1 the value is the more shiny the material
	 *            appears in the light
	 */
	public void setShininess(float shininess) {
		this._shininess = MathUtils.clamp(shininess, Defaults.Materials.SHININESS_MIN,
				Defaults.Materials.SHININESS_MAX);
	}

	/**
	 * @return the specular color for this material
	 */
	public Vector3f getSpecularColor() {
		return _specularColor;
	}

	/**
	 * Sets the specular color for this material is the color that appears when
	 * the light is shining into it by calculating the specular value and
	 * multiplying it against the color. For no additional shining color this
	 * should be black (0, 0, 0)
	 * 
	 * @param r
	 *            RED-value [0-1]
	 * @param g
	 *            GREEN-value [0-1]
	 * @param b
	 *            BLUE-value [0-1]
	 */
	public void setSpecularColor(float r, float g, float b) {
		this._specularColor.set(r, g, b);
	}

	/**
	 * Compares the material for equality against the current material
	 * 
	 * @param mat
	 *            the material to compare
	 * @return true if they are equal in content
	 */
	public boolean compare(Material mat) {
		return false;
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
