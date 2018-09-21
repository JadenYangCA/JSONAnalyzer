package github.com.jadenyangca.analyzer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesUtil {
	private static Logger logger = LogManager.getLogger();
	
	//configuration file name
	private static final String configFileName = "config.properties";
	
	//configuration file path
	private final String configFilePath = PropertiesUtil.class.getClassLoader().getResource(configFileName).getFile();
	
	//configuration file object
	private final File configFile = new File(configFilePath);
	
	// last Modified Time of configuration file 
	private long lastModifiedTime = 0;
	
	private Properties props = null;

	// singleton instance 
	private static PropertiesUtil instance = new PropertiesUtil();

	/**
	 * private constructor
	 */
	private PropertiesUtil() {
		props = new Properties();
		try {
			props.load(new FileInputStream(configFile));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * get singleton instance
	 * @return PropertiesUtil instance
	 */
	public static synchronized PropertiesUtil getInstance() {
//		logger.info("user.dir1:" + System.getProperty("user.dir"));
		return instance;
	}

	/**
	 * get property value
	 * @param keyName property name
	 * @return property value
	 */
	public String getValue(String keyName) {
		return props.getProperty(keyName);
	}
	
	/**
	 * get the newest value by name, reload the configuration if it is modified
	 * @param keyName property key name
	 * @param defaultVal property value if it doesn't exist
	 * @return property value
	 */
	public String getValueNewest(String keyName, String defaultVal) {
		long newTime = configFile.lastModified();
		// check whether configuration file is modified, if it is, reload it
		if (newTime == 0) {
			// configuration file is not existent
			if (lastModifiedTime == 0) {
				logger.error(configFilePath + " file does not exist !");
			} else {
				logger.error(configFilePath + " file was deleted !");
			}

			return defaultVal;
		} else if (newTime > lastModifiedTime) {
			// reload configuration file
			props.clear();
			try {
				props.load(new FileInputStream(configFile));
			} catch (Exception e) {
				logger.error(e);
			}
		}
		lastModifiedTime = newTime;
		return getValue(keyName, defaultVal);
	}

	/**
	 * get the value that may be out of date 
	 * @param keyName property key name
	 * @param defaultVal property value if it doesn't exist
	 * @return property value
	 */
	public String getValue(String keyName, String defaultValue) {
		String val = props.getProperty(keyName);
		if (val == null) {
			return defaultValue;
		} else {
			return val;
		}
	}

	/*
	 * get all keys and values from configuration
	 */
	public Map<String, String> getAllKeyAndValue() {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement().toString();
			String value = props.getProperty(key);
			map.put(key, value);
		}
		return map;
	}
	
	/**
	public void setValue(String itemName, String value) {
		props.setProperty(itemName, value);
		return;
	}*/
	
//	public Map<String, String> getAllKeyAndValue2() {
//		Map<String, String> map = new HashMap<String, String>();
//		props.list(System.out);
//		Object[] objs = props.keySet().toArray();
//		for (int i = 0; i < objs.length; i++) {
//			map.put(objs[i].toString(), props.get(objs[i]).toString());
//		}
//		return map;
//	}

//	public static void main(String[] args) {
//		Properties pps = System.getProperties();
//		pps.list(System.out);
//	}
}
