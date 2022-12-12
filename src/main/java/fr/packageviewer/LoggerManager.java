package fr.packageviewer;

import java.util.logging.*;

public class LoggerManager {

	private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

	public static Logger getLogger(String name){
		return getLogger(name, DEFAULT_LOG_LEVEL);
	}

	public static Logger getLogger(String name, Level level){
		Logger logger = Logger.getLogger(name);
		logger.setLevel(level);

		Handler handler = new StreamHandler(System.err, new SimpleFormatter());
		logger.addHandler(handler);

		logger.setUseParentHandlers(false);
		return logger;
	}
}
