package engine.utils.loaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import engine.graphics.geo.Texture;
import engine.utils.ResourceManager;

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
		TextureData data = null;
		Texture.TextureOptions options = new Texture.TextureOptions();

		// Get the path to the texture file
		String pathToFile = ResourceManager.TEXTURES_PATH + fileName;

		// Parses the file type so we can support multiple types
		switch (ResourceManager.getFileType(fileName)) {
		case PNG:
			data = decodePNGTexture(new FileInputStream(pathToFile));
			break;
		default:
			throw new Exception(String.format("Trying to load an invalid file type: %s as a texture.", fileName));
		}

		// The texture class deals with binding it to openGL for us
		return new Texture(data.getWidth(), data.getHeight(), data.getByteBuffer(), options);
	}

	/*
	 * Helper function to load a png file
	 */
	private static TextureData decodePNGTexture(FileInputStream inputStream) throws IOException {
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
