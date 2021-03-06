package engine.resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Anything dealing with the loading of resources
 * 
 * @author brandon.porter
 *
 */
public final class ResourceManager {
	/**
	 * An array of supported file types for the engine
	 */
	public static final FileType[] SUPPORTED_FILE_TYPES = FileType.values();

	public static final String RESOURCES_PATH = "/";
	public static final String SHADERS_PATH = "shaders/";
	public static final String MODELS_PATH = "models/";
	public static final String TEXTURES_PATH = "textures/";

	/*
	 * Prevent instantiation
	 */
	private ResourceManager() {
	}

	/**
	 * Loads the found shader file as a String
	 * 
	 * @param fileName
	 *            file name of the shader
	 * @return shader file as one big string
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String loadShaderFile(String fileName) throws FileNotFoundException, IOException {
		return loadResourceAsString(SHADERS_PATH + fileName);
	}

	/**
	 * Loads and returns the complete file as a String
	 * 
	 * @param relFilePath
	 *            full relative file path to the resources directory
	 * @return resource file as one big string
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String loadResourceAsString(String relFilePath) throws FileNotFoundException, IOException {
		String result = "";
		Scanner scanner = null;

		try (InputStream in = loadResourceAsStream(relFilePath)) {
			scanner = new Scanner(in, "UTF-8");
			// TODO: More information on why useDelimter("\\A")
			result = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return result;
	}

	/**
	 * Loads the model file to read and return all lines as a list of strings
	 * 
	 * @param fileName
	 *            filename of the model
	 * @return all lines in the model file
	 * @throws IOException
	 */
	public static List<String> loadModel(String fileName) throws IOException {
		return loadResourceAsStringList(MODELS_PATH + fileName);
	}

	/**
	 * Loads and return the resource file as a list of strings
	 * 
	 * @param relFilePath
	 *            full relative file path to the resources directory
	 * @return parsed file into a list of strings
	 * @throws IOException
	 */
	public static List<String> loadResourceAsStringList(String relFilePath) throws IOException {
		List<String> lines = new ArrayList<>();

		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(loadResourceAsStream(relFilePath)))) {
			while ((line = br.readLine()) != null)
				lines.add(line);
		}

		return lines;
	}

	/**
	 * Loads the specified resource as a stream
	 * 
	 * @param relFilePath
	 *            path of resource relative to resources folder
	 * @return resource as input stream
	 */
	public static InputStream loadResourceAsStream(String relFilePath) {
		return ResourceManager.class.getResourceAsStream(RESOURCES_PATH + relFilePath);
	}

	/**
	 * Checks the filename extensions against our supported file types, and
	 * returns the first file type with the same file extension, if empty
	 * extension or does not exist it will return FileType.UNKNOWN
	 * 
	 * @param fileName
	 *            name of the file to get fileType
	 * @return file type for the associated file name
	 */
	public static FileType getFileType(String fileName) {

		String fileExt = fileName.substring(fileName.lastIndexOf("."));

		// Loop over each supported file type
		for (FileType fileType : SUPPORTED_FILE_TYPES) {
			for (String ext : fileType.extensions()) {
				if (fileExt.equalsIgnoreCase(ext))
					return fileType;
			}
		}

		return FileType.UNKNOWN;
	}

	/**
	 * Keeps track of all of the file types our engine supports
	 * 
	 * @author brandon.porter
	 *
	 */
	public static enum FileType {
		UNKNOWN(""),
		// Game Objects
		OBJ(".obj"),
		// Images
		PNG(".png"),
		// Shaders
		VERTEX_SHADER(".vert"), FRAGMENT_SHADER(".frag");

		private final String[] _extensions;

		/*
		 * Constructs a new file type
		 */
		private FileType(String... exts) {
			this._extensions = exts;
		}

		/**
		 * @return all supported extensions for this file type
		 */
		public String[] extensions() {
			return _extensions;
		}
	}
}
