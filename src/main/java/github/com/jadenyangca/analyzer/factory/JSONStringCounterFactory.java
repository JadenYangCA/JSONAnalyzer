package github.com.jadenyangca.analyzer.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import github.com.jadenyangca.analyzer.impl.FastJSONStringCounter;
import github.com.jadenyangca.analyzer.impl.RegexJSONStringCounter;
import github.com.jadenyangca.analyzer.interfaces.JSONStringCounterInterface;

/**
 * To generate the JSONStringCounter instance
 * @author Jaden
 */
public class JSONStringCounterFactory {
	public static final String ARG_FAST = "fast";
	public static final String ARG_REGEX = "regex";
	
	private JSONStringCounterFactory() {
	}

	/**
	 * create instance for JSON string Counter
	 * @param mod argument must be fast or regex
	 * @return JSON string Counter
	 */
	public static JSONStringCounterInterface getInstance(String mod) {
		Logger logger = LogManager.getLogger();
		//** fast  - Use FastJSON to count metadata types efficiently if JSON file is huge avoiding java.lang.OutOfMemoryError.
		if (mod.toLowerCase().equals(ARG_FAST)) {
			return new FastJSONStringCounter();

		//** regex - Use regular expression to analyze JSON string if the format is damaged
		} else if (mod.toLowerCase().equals(ARG_REGEX)) {
			return new RegexJSONStringCounter();
		} else {
			logger.error("* the 2nd argument must be fast or regex");
			return null;
		}
	}
}
