package engine.rendering.material;

import org.joml.Vector3fc;

import engine.assets.Texture;
import engine.rendering.material.MaterialProperty.FloatProperty;
import engine.rendering.material.MaterialProperty.TextureProperty;
import engine.rendering.material.MaterialProperty.Vector3fProperty;
import engine.rendering.shader.UniformType;
import engine.system.Defaults;
import engine.utils.math.MathUtils;
import engine.utils.math.VectorUtils;

/**
 * Defines a list of material properties to be changed per renderer. Using this
 * to change material properties instead of changing the material directly will
 * be more performant unless you want to change the material properties for
 * every gameObject using that material
 * 
 * @author Brandon Porter
 *
 */
public class MaterialPropertyBlock {
	private TextureProperty _mainTexture = null;
	private Vector3fProperty _color = null;
	private Vector3fProperty _specularColor = null;
	private FloatProperty _shininess = null;

	/**
	 * Constructs a new material property block
	 */
	public MaterialPropertyBlock() {
	}

	/**
	 * Creates a new properties block by copying the properties from the passed
	 * in block
	 * 
	 * @param materialPropertyBlock
	 *            the property block to clone from, its contents will not be
	 *            changed
	 */
	public MaterialPropertyBlock(MaterialPropertyBlock materialPropertyBlock) {
		cloneFrom(materialPropertyBlock);
	}

	/**
	 * Clones all the values from the passed in properties to this current
	 * instance
	 * 
	 * @param materialPropertyBlock
	 *            the property block to clone from, its contents will not be
	 *            changed
	 */
	public void cloneFrom(MaterialPropertyBlock materialPropertyBlock) {
		this.setMainTexture(materialPropertyBlock.getMainTexture());
		this.setColor(materialPropertyBlock.getColor());
		this.setSpecularColor(materialPropertyBlock.getSpecularColor());
		this.setShininess(materialPropertyBlock.getShininess());
	}

	/**
	 * Compares the material properties for equality against the current
	 * property block
	 * 
	 * @param props
	 *            the property block to compare
	 * @return true if they are equal in instance or property values
	 */
	public boolean compare(MaterialPropertyBlock props) {
		if (this == props)
			return true;

		return getMainTexture() == props.getMainTexture() && VectorUtils.compare(getColor(), props.getColor())
				&& VectorUtils.compare(getSpecularColor(), props.getSpecularColor())
				&& getShininess() == props.getShininess();
	}

	/**
	 * Sets the texture of the meshes using this material
	 * 
	 * @param texture
	 *            the texture to display on all meshes using this material
	 */
	public void setMainTexture(Texture texture) {
		this._mainTexture = TextureProperty.set(_mainTexture, UniformType.MAIN_TEXTURE, texture);
	}

	/**
	 * @return the texture of the mesh using this material, or null if none set
	 */
	public Texture getMainTexture() {
		return getMainTextureOr(null);
	}

	/**
	 * Does a null check against the current texture and returns the texture
	 * value if not null, otherwise if it is null it will return the value of
	 * the passed in texture.
	 * 
	 * @param texture
	 *            default "fallback" texture to return if current texture is
	 *            null
	 * @return current texture if not null otherwise the fallback texture
	 */
	public Texture getMainTextureOr(Texture texture) {
		return _mainTexture == null || _mainTexture.value == null ? texture : _mainTexture.value;
	}

	/**
	 * Sets the color of the material
	 * 
	 * @param color
	 *            the color to set (x = red [0-1], y = green [0-1], z = blue
	 *            [0-1]). The vector instance is not stored, its values are
	 *            copied.
	 */
	public void setColor(Vector3fc color) {
		if (color == null)
			this._color = null;
		else
			setColor(color.x(), color.y(), color.z());
	}

	/**
	 * Sets the color of the material
	 * 
	 * @param r
	 *            RED-value [0-1]
	 * @param g
	 *            GREEN-value [0-1]
	 * @param b
	 *            BLUE-value [0-1]
	 */
	public void setColor(float r, float g, float b) {
		this._color = Vector3fProperty.set(_color, UniformType.COLOR, r, g, b);
	}

	/**
	 * @return the color of the material, or null if none exists
	 */
	public Vector3fc getColor() {
		return getColorOr(null);
	}

	/**
	 * Does a null check against the current color and returns the color value
	 * if not null, otherwise if it is null it will return the value of the
	 * passed in color.
	 * 
	 * @param color
	 *            default "fallback" color to return if current color is null
	 * @return current color if not null otherwise the fallback color
	 */
	public Vector3fc getColorOr(Vector3fc color) {
		return _color == null || _color.value == null ? color : _color.value;
	}

	/**
	 * Sets the specular color for this material is the color that appears when
	 * the light is shining into it by calculating the specular value and
	 * multiplying it against the color. For no additional shining color this
	 * should be black (0, 0, 0)
	 * 
	 * @param color
	 *            the color to set (x = red [0-1], y = green [0-1], z = blue
	 *            [0-1]). The vector instance is not stored, its values are
	 *            copied.
	 */
	public void setSpecularColor(Vector3fc color) {
		if (color == null)
			this._specularColor = null;
		else
			setSpecularColor(color.x(), color.y(), color.z());
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
		this._specularColor = Vector3fProperty.set(_specularColor, UniformType.SPECULAR_COLOR, r, g, b);
	}

	/**
	 * @return the specular color of the material, or null if none exists
	 */
	public Vector3fc getSpecularColor() {
		return getSpeculaColorOr(null);
	}

	/**
	 * Does a null check against the current specular color and returns the
	 * specular color value if not null, otherwise if it is null it will return
	 * the value of the passed in specular color.
	 * 
	 * @param color
	 *            default "fallback" specular color to return if current
	 *            specular color is null
	 * @return current specular color if not null otherwise the fallback
	 *         specular color
	 */
	public Vector3fc getSpeculaColorOr(Vector3fc color) {
		return _specularColor == null || _specularColor.value == null ? color : _specularColor.value;
	}

	/**
	 * Sets the shininess factor of this material [0.01 - 1.0]
	 * 
	 * @param shininess
	 *            the closer to 1 the value is the more shiny the material
	 *            appears in the light
	 */
	public void setShininess(float shininess) {
		float clampedValue = MathUtils.clamp(shininess, Defaults.Materials.SHININESS_MIN,
				Defaults.Materials.SHININESS_MAX);

		this._shininess = FloatProperty.set(_shininess, UniformType.SHININESS, clampedValue);
	}

	/**
	 * @return The shininess factor of the material [0.01 - 1.0], or null if
	 *         none
	 */
	public float getShininess() {
		return getShininessOr(Defaults.Materials.SHININESS_MIN);
	}

	/**
	 * Does a null check against the current shininess factor and returns the
	 * shininess factor value if not null, otherwise if it is null it will
	 * return the value of the passed in shininess factor.
	 * 
	 * @param color
	 *            default "fallback" shininess factor to return if current
	 *            shininess factor is null
	 * @return current shininess factor if not null otherwise the fallback
	 *         shininess factor
	 */
	public float getShininessOr(float shininess) {
		return _shininess == null ? shininess : _shininess.value;
	}
}
