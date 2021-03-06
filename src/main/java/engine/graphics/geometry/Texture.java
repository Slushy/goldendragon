package engine.graphics.geometry;

import org.lwjgl.opengl.GL11;

import engine.common.Entity;

/**
 * Represents a texture being stored in graphics VRAM
 * 
 * @author Brandon
 *
 */
public class Texture extends Entity {
	private static final String ENTITY_NAME = "Texture";

	private int _id = -1;
	private int _width;
	private int _height;

	/**
	 * Constructs a new texture with the name "Texture"
	 */
	public Texture() {
		this(ENTITY_NAME);
	}

	/**
	 * Constructs a new texture with the specified name
	 * 
	 * @param name
	 *            the name of the texture
	 */
	public Texture(String name) {
		super(name);
	}

	/**
	 * A texture is loaded if it has a valid ID that was generated from the
	 * graphics library
	 * 
	 * @return true if texture is loaded and ready, false otherwise
	 */
	public final boolean isLoaded() {
		return getTextureId() >= 0;
	}
	
	/**
	 * @return OpenGL reference id for the texture
	 */
	public int getTextureId() {
		return _id;
	}

	/**
	 * Sets the OpenGL reference Id for the texture
	 * 
	 * @param id
	 *            the unique id for the graphics library to reference
	 */
	public void setTextureId(int id) {
		this._id = id;
	}

	/**
	 * @return width of texture
	 */
	public int getWidth() {
		return _width;
	}

	/**
	 * Sets the width of the texture
	 * 
	 * @param width
	 *            width in pixels
	 */
	public void setWidth(int width) {
		this._width = width;
	}

	/**
	 * @return height of texture
	 */
	public int getHeight() {
		return _height;
	}

	/**
	 * Sets the height of the texture
	 * 
	 * @param height
	 *            height in pixels
	 */
	public void setHeight(int height) {
		this._height = height;
	}

	/**
	 * Removes texture from graphics vram
	 */
	@Override
	protected void onDispose() {
		if (isLoaded())
			GL11.glDeleteTextures(_id);
	}

	/**
	 * Additional options to initialize a texture
	 * 
	 * @author Brandon
	 *
	 */
	public static class TextureOptions {
		/**
		 * Default options to pass through to a builder without creating
		 * multiple instances
		 */
		public static final TextureOptions Default = new TextureOptions();

		/**
		 * Whether or not the minFilter and magFilter rules are used
		 */
		public boolean filtersEnabled = true;

		/**
		 * Texture minifying function that is used whenever the pixel being
		 * textured maps to an area greater than one texture element
		 */
		public int minFilterRule = GL11.GL_NEAREST;

		/**
		 * Texture magnification function that is used whenever a pixel being
		 * textured maps to an area less than or equal to one texture element
		 */
		public int magFilterRule = GL11.GL_NEAREST;

		/**
		 * Specifies a fixed bias value that is to be added to the
		 * level-of-detail parameter for the texture before texture sampling.
		 * Greater than 0 is more detail, less than 0 is less detail
		 */
		public float levelOfDetailBias = 0.0f;

		/**
		 * Whether or not to enable mipmapping. A mipmap is a decreasing
		 * resolution set of images generated froma high detailed texture which
		 * will be used automatically when the object is scaled
		 */
		public boolean useMipmap = true;
	}
}
