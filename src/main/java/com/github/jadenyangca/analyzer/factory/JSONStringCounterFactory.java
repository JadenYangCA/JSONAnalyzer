package com.github.jadenyangca.analyzer.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.jadenyangca.analyzer.interfaces.JSONStringCounterInterface;
import com.github.jadenyangca.analyzer.utils.PropertiesUtil;

/**
 * To generate the JSONStringCounter instance
 * 
 * @author Jaden
 */
public class JSONStringCounterFactory {
	// prefix in the configuration file
	public static final String FACTORY_PREFIX = "factory.";

	/**
	 * private constructor
	 */
	private JSONStringCounterFactory() {
	}

	/**
	 * create instance for JSON string Counter
	 * 
	 * @param mod argument must be fast or regex
	 * @return JSON string Counter
	 */
	public static JSONStringCounterInterface getInstance(String mod) {
		Logger logger = LogManager.getLogger();
		String className = PropertiesUtil.getInstance().getValue("factory." + mod);
		// ** fast - Use FastJSON to count metadata types efficiently if JSON file is huge avoiding java.lang.OutOfMemoryError.
		// ** regex - Use regular expression to analyze JSON string if the format is damaged
		if (className == null || className.isEmpty()) {
			return null;
		} else {
			try {
				return (JSONStringCounterInterface) Class.forName(className).newInstance();
			} catch (ClassNotFoundException e) {
				logger.error("class name is not right. please check it in the configuration file.");
				logger.error(e);
				return null;
			} catch (InstantiationException e) {
				logger.error(e);
				return null;
			} catch (IllegalAccessException e) {
				logger.error(e);
				return null;
			}
		}
	}
}
