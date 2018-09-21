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
	private static final String configFileName = "config.properties";
	private final String configFilePath = PropertiesUtil.class.getClassLoader().getResource(configFileName).getFile();
	//对应于属性文件的文件对象变量
	private final File configFile = new File(configFilePath);
	//属性文件的最后修改日期
	private long lastModifiedTime = 0;
	//属性文件所对应的属性对象变量
	private Properties props = null;
	
	//本类可能存在的唯一的唯一实例
	private static PropertiesUtil instance = new PropertiesUtil();
	
	/**
	 * 私有构造方法，用以保证外界无法直接实例化
	 */
	private PropertiesUtil() {
		props = new Properties();
		try {
			props.load(new FileInputStream(configFile));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static synchronized PropertiesUtil getInstance() {
//		logger.info("user.dir1:" + System.getProperty("user.dir"));
		return instance;
	}

	public String getValueNewest(String keyName,String defaultVal) {
		long newTime = configFile.lastModified();
		//检查属性文件是否被其他的程序修改过，如果是，重读此配置文件
		if (newTime == 0) {
			//属性文件不存在
			if (lastModifiedTime == 0) {
				logger.error(configFilePath + " file does not exist !");
			} else {
				logger.error(configFilePath + " file was deleted !");
			}
			
			return defaultVal;
		} else if (newTime > lastModifiedTime) {
			//属性文件被修改过,重新加载配置文件
			props.clear();
			try {
				props.load(new FileInputStream(configFile));
			}  catch (Exception e) {
				logger.error(e);
			}
		}
		lastModifiedTime = newTime;
		return getValue(keyName, defaultVal);
	}
	

	public String getValue(String keyName) {
		return props.getProperty(keyName);
	}
	
	public String getValue(String keyName, String defaultValue) {
		String val = props.getProperty(keyName);
		if (val == null) {
			return defaultValue;
		} else {
			return val;
		}
	}
	

	public void setValue(String itemName,String value) {
		props.setProperty(itemName, value);
		return;
	}
	
	public Map<String, String> getAllKeyAndValue() {
		Map<String, String> map = new HashMap<String, String>();
		//遍历
		Enumeration<?> en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement().toString();//key值
			String value = props.getProperty(key);
			map.put(key, value);
		}
		return map;
	}
	
	public Map<String, String> getAllKeyAndValue2() {
		Map<String, String> map = new HashMap<String, String>();
		props.list(System.out);
		Object[] objs = props.keySet().toArray();
		for (int i = 0; i < objs.length; i++) {
			map.put(objs[i].toString(), props.get(objs[i]).toString());
		}
		return map;
	}
	
	public static void main(String[] args) {
		
		//1.获得java虚拟机的参数
		Properties pps = System.getProperties();
		pps.list(System.out);
		
	}
}
