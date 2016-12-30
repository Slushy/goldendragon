package engine.graphics.geo;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

/**
 * Represents a texture being stored in graphics VRAM
 * 
 * @author Brandon
 *
 */
public class Texture implements IBindable {
	private final int _id;
	private final int _width;
	private final int _height;

	/**
	 * Constructs a new texture
	 * 
	 * @param width
	 *            the width of the texture in pixels
	 * @param height
	 *            the height of the texture in pixels
	 * @param buffer
	 *            the bytes of the image
	 */
	public Texture(int width, int height, ByteBuffer buffer) {
		this(width, height, buffer, new TextureOptions());
	}

	/**
	 * Constructs a new texture
	 * 
	 * @param width
	 *            the width of the texture in pixels
	 * @param height
	 *            the height of the texture in pixels
	 * @param buffer
	 *            the bytes of the image
	 * @param options
	 *            additional options to initialize the texture
	 */
	public Texture(int width, int height, ByteBuffer buffer, TextureOptions options) {
		this._id = GL11.glGenTextures();
		this._width = width;
		this._height = height;

		// Initialize texture with openGL
		init(buffer, options);
	}

	/**
	 * Readies the texture for use with opengl
	 */
	@Override
	public void use() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _id);
	}

	/**
	 * Clears the active texture
	 */
	@Override
	public void done() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * @return OpenGL reference id for the texture
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * @return width of texture
	 */
	public int getWidth() {
		return _width;
	}
	
	/**
	 * @return height of texture
	 */
	public int getHeight() {
		return _height;
	}
	

	/**
	 * Removes texture from graphics vram
	 */
	@Override
	public void dispose() {
		GL11.glDeleteTextures(_id);
	}

	/**
	 * Initializes texture options with OpenGL
	 * 
	 * @param buffer
	 *            the bytes of the image
	 * @param options
	 *            additional options to initialize the texture
	 */
	protected void init(ByteBuffer buffer, TextureOptions options) {
		use();

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte
		// size
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Load texture data to VRAM
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, _width, _height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				buffer);

		// Generate a mipmap for texture
		if (options.useMipmap) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		}

		// Not always necessary: this basically says that when a pixel is drawn
		// with no direct one to one association to a texture coordinate it will
		// pick the nearest texture coordinate point.
		if (options.filtersEnabled) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, options.minFilterRule);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, options.magFilterRule);
		}

		// Set level of detail bias
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, options.levelOfDetailBias);
		
		done();
	}

	/**
	 * Additional options to initialize a texture
	 * 
	 * @author Brandon
	 *
	 */
	public static class TextureOptions {
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
