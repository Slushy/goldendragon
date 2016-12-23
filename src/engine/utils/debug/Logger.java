package engine.utils.debug;

/**
 * Each class should have an instance of logger used for debugging
 * 
 * @author brandon.porter
 *
 */
public class Logger {
	public static LoggerLevel GLOBAL_LOGGER_LEVEL = LoggerLevel.WARN;

	private final String _className;
	private final LoggerLevel _loggerLevel;

	/**
	 * Constructs an instance of the logger for debugging
	 * 
	 * @param className
	 *            name of the class holding the instance of the logger
	 */
	public Logger(String className) {
		this(className, GLOBAL_LOGGER_LEVEL);
	}

	/**
	 * Constructs an instance of the logger for debugging
	 * 
	 * @param className
	 *            name of the class holding the instance of the logger
	 * @param loggerLevel
	 *            level to print messages for
	 */
	public Logger(String className, LoggerLevel loggerLevel) {
		this._className = className;
		this._loggerLevel = loggerLevel;
	}

	/**
	 * Prints a DEBUG message, mainly used to log information
	 * 
	 * @param message
	 *            message to print
	 */
	public void debug(String message) {
		debug(message, null);
	}

	/**
	 * Prints a DEBUG message, mainly used to log information
	 * 
	 * @param message
	 *            message to print
	 * @param args
	 *            additional arguments to include in formatted message
	 */
	public void debug(String message, Object... args) {
		if (_loggerLevel == LoggerLevel.DEBUG)
			System.out.println(formatMessage(LoggerLevel.DEBUG, message, args));
	}

	/**
	 * Prints a WARNING message, mainly used to notify the user about a
	 * non-breaking issue
	 * 
	 * @param message
	 *            message to print
	 */
	public void warn(String message) {
		warn(message, null);
	}

	/**
	 * Prints a WARNING message, mainly used to notify the user about a
	 * non-breaking issue
	 * 
	 * @param message
	 *            message to print
	 * @param args
	 *            additional arguments to include in formatted message
	 */
	public void warn(String message, Object... args) {
		if (_loggerLevel != LoggerLevel.IGNORE)
			System.out.println(formatMessage(LoggerLevel.WARN, message, args));
	}

	/*
	 * Format and return a logging message
	 */
	private String formatMessage(LoggerLevel messageLevel, String message, Object... args) {
		String identifier = String.format("%s [%s]: ", _className, messageLevel);
		return identifier + String.format(message, args);
	}
}
