package engine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Anything dealing with the loading of resources
 * @author brandon.porter
 *
 */
public class ResourceManager {
	private final static String RESOURCES_PATH = "/resources";
	private final static String SHADERS_PATH = RESOURCES_PATH + "/shaders";
	
	/**
	 * Loads the found shader file as a String
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String loadShaderFile(String fileName) throws FileNotFoundException, IOException {
		return loadFileAsString(SHADERS_PATH + "/" + fileName);
	}
	
	/*
	 * Loads and returns the complete file as a String
	 */
	private static String loadFileAsString(String fullFilePath) throws FileNotFoundException, IOException {
		String result = "";
		Scanner scanner = null;
		try (InputStream in = new FileInputStream(fullFilePath)) {
			scanner = new Scanner(in, "UTF-8");
			// TODO: More information on why useDelimter("\\A")
			result = scanner.useDelimiter("\\A").next();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return result;
	}
}
