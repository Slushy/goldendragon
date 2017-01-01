package engine.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Simple efficient way to log messages used for debugging
 * 
 * @author Brandon Porter
 *
 */
public final class Debug {
	public static boolean ENABLED = false;
	private static final DateFormat STAMP_FORMAT = new SimpleDateFormat("hh:mm:ss.SSS");

	// Static class
	private Debug() {
	}

	/**
	 * Prints out a message useful for debugging
	 * 
	 * @param message
	 *            message to print to console
	 */
	public static void log(String message) {
		if (ENABLED)
			System.out.println(currentTimeStamp() + " [LOG] - " + message);
	}

	/**
	 * Prints out a warning message useful for debugging
	 * 
	 * @param warning
	 *            warning to print to console
	 */
	public static void warn(String warning) {
		if (ENABLED)
			System.out.println(currentTimeStamp() + " [WARN] - " + warning);
	}

	/**
	 * PRintes out an error message useful for debugging
	 * 
	 * @param error
	 *            error to print to console
	 */
	public static void error(String error) {
		System.err.println(currentTimeStamp() + " [ERROR] - " + error);
	}

	/*
	 * Returns the current time stamp of the message
	 */
	private static String currentTimeStamp() {
		return STAMP_FORMAT.format(System.currentTimeMillis());
	}
}
