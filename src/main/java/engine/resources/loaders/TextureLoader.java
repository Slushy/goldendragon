package engine.resources.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import engine.graphics.geometry.Texture;
import engine.resources.RequestManager;
import engine.resources.ResourceManager;
import engine.utils.Debug;

/**
 * Parses texture files of supported types and loads them into memory as new
 * texture objects
 * 
 * @author brandon.porter
 *
 */
public class TextureLoader {

	/*
	 * Private to prevent instantiation
	 */
	private TextureLoader() {
	}

	/**
	 * Loads the specified file as a texture
	 * 
	 * @param fileName
	 *            file name of the texture
	 * @return new texture object
	 * @throws Exception
	 */
	public static Texture loadTexture(String fileName) throws Exception {
		return loadTexture(fileName, Texture.TextureOptions.Default);
	}

	/**
	 * Loads the specified file as a texture
	 * 
	 * @param fileName
	 *            file name of the texture
	 * @param textureOptions
	 *            the additional options to load the texture with
	 * @return new texture object
	 * @throws Exception
	 */
	public static Texture loadTexture(String fileName, Texture.TextureOptions textureOptions) throws Exception {
		Texture texture = new Texture(fileName);
		loadTexture(texture);
		return texture;
	}

	/**
	 * Loads the existing texture from disk and into graphics memory
	 * 
	 * @param texture
	 *            the existing texture object
	 * @throws Exception
	 */
	public static void loadTexture(Texture texture) throws Exception {
		loadTexture(texture, Texture.TextureOptions.Default);
	}

	/**
	 * Loads the existing texture from disk and into graphics memory
	 * 
	 * @param texture
	 *            the existing texture object
	 * @param textureOptions
	 *            the additional options to load the texture with
	 * @throws Exception
	 */
	public static void loadTexture(Texture texture, Texture.TextureOptions textureOptions) throws Exception {
		TextureData data; // Must be

		// Get the path to the texture file
		String fileName = texture.getFileName();
		String pathToFile = ResourceManager.TEXTURES_PATH + fileName;

		// Parses the file type so we can support multiple types
		switch (ResourceManager.getFileType(fileName)) {
		case PNG:
			data = decodePNGTexture(ResourceManager.loadResourceAsStream(pathToFile));
			break;
		default:
			throw new Exception(String.format("Trying to load an invalid file type: %s as a texture.", fileName));
		}

		texture.setWidth(data.getWidth());
		texture.setHeight(data.getHeight());

		// This needs to be on main thread
		boolean wasImmediate = RequestManager.makeGLRequestImmediate(() -> {
			int id = registerTextureWithOpenGL(data, textureOptions);
			texture.setTextureId(id);
		});
		
		Debug.log("GL request to register texture (" + texture.getName() + ") was immediate: " + wasImmediate);
	}

	/*
	 * Helper function to load a png file
	 */
	private static TextureData decodePNGTexture(InputStream inputStream) throws IOException {
		// Load png file into decoder instance
		PNGDecoder decoder = new PNGDecoder(inputStream);

		// Load texture contents into a byte buffer(4 bytes per pixel - RGBA)
		int bytesPerPixel = 4;
		ByteBuffer buffer = ByteBuffer.allocateDirect(bytesPerPixel * decoder.getWidth() * decoder.getHeight());
		decoder.decode(buffer, decoder.getWidth() * bytesPerPixel, Format.RGBA);
		buffer.flip();

		// return the data
		return new TextureData(decoder.getWidth(), decoder.getHeight(), buffer);
	}

	/*
	 * Registers the texture with opengl. [WARNING] - This MUST be called from
	 * the main thread.
	 */
	private static int registerTextureWithOpenGL(TextureData data, Texture.TextureOptions options) {
		// Create and bind the new texture
		int textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte
		// size
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Load texture data to VRAM
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, data.getByteBuffer());

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

		// Unbind and return the texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return textureId;
	}

	/**
	 * Container used to hold data for creating a new texture. Only to be used
	 * by this loader class
	 * 
	 * @author Brandon
	 *
	 */
	private static class TextureData {
		private final int _width;
		private final int _height;
		private final ByteBuffer _buffer;

		/**
		 * Constructs a new texture data container
		 * 
		 * @param width
		 *            the width of the texture in pixels
		 * @param height
		 *            the height of the texture in pixels
		 * @param buffer
		 *            the bytes of the image
		 */
		public TextureData(int width, int height, ByteBuffer buffer) {
			this._width = width;
			this._height = height;
			this._buffer = buffer;
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
		 * @return texture data in bytes
		 */
		public ByteBuffer getByteBuffer() {
			return _buffer;
		}
	}
}
