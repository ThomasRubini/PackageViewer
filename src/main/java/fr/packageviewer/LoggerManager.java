package fr.packageviewer;

import java.util.logging.*;

/**
 * The LoggerManager class allows for basic debug output management using
 * Java's default logging class.
 * 
 * @author R.Thomas
 * @version 1.0
 */
public class LoggerManager {
	/**
	 * Default log level, should be INFO
	 */
	private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

	/**
	 * Static factory for the Logger class
	 * 
	 * @param name String, of the logger to create
	 * @return Logger, a new logger
	 */
	public static Logger getLogger(String name) {
		return getLogger(name, DEFAULT_LOG_LEVEL);
	}

	/**
	 * Main static factory for the Logger class
	 *
	 * @param name  String, name of the logger to create
	 * @param level Level, the level of severity of the logger
	 * @return Logger, a new logger
	 */
	public static Logger getLogger(String name, Level level) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(level);

		Handler handler = new StreamHandler(System.err, new SimpleFormatter());
		logger.addHandler(handler);

		logger.setUseParentHandlers(false);
		return logger;
	}
}
