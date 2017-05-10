package engine.utils;

/**
 * Utility helper for common time-related calculations
 * 
 * @author Brandon Porter
 *
 */
public final class TimeUtils {

	// Static class
	private TimeUtils() {
	}

	/**
	 * Converts time in milliseconds to nanoseconds
	 * 
	 * @param ms
	 *            time in milliseconds
	 * @return same time in nanoseconds
	 */
	public static final double MilliToNano(double ms) {
		return ms * 1_000_000.0;
	}

	/**
	 * Converts time in nanoseconds to milliseconds
	 * 
	 * @param ns
	 *            time in nanoseconds
	 * @return same time in milliseconds
	 */
	public static final double NanoToMilli(double ns) {
		return ns / 1_000_000.0;
	}

	/**
	 * Converts time in nanoseconds to seconds
	 * 
	 * @param ns
	 *            time in nanoseconds
	 * @return same time in seconds
	 */
	public static final double NanoToSeconds(double ns) {
		return ns / 1_000_000_000.0;
	}
	
	/**
	 * Gets the overall time in seconds
	 * 
	 * @return overall clock time
	 */
	public static double getSystemTimeSeconds() {
		return NanoToSeconds(getSystemTimeNS());
	}

	/**
	 * @return the overall time in nanoseconds
	 */
	public static long getSystemTimeNS() {
		return System.nanoTime();
	}
}
