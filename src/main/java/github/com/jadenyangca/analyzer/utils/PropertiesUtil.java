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
	//��Ӧ�������ļ����ļ��������
	private final File configFile = new File(configFilePath);
	//�����ļ�������޸�����
	private long lastModifiedTime = 0;
	//�����ļ�����Ӧ�����Զ������
	private Properties props = null;
	
	//������ܴ��ڵ�Ψһ��Ψһʵ��
	private static PropertiesUtil instance = new PropertiesUtil();
	
	/**
	 * ˽�й��췽�������Ա�֤����޷�ֱ��ʵ����
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
		//��������ļ��Ƿ������ĳ����޸Ĺ�������ǣ��ض��������ļ�
		if (newTime == 0) {
			//�����ļ�������
			if (lastModifiedTime == 0) {
				logger.error(configFilePath + " file does not exist !");
			} else {
				logger.error(configFilePath + " file was deleted !");
			}
			
			return defaultVal;
		} else if (newTime > lastModifiedTime) {
			//�����ļ����޸Ĺ�,���¼��������ļ�
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
		//����
		Enumeration<?> en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement().toString();//keyֵ
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
		
		//1.���java������Ĳ���
		Properties pps = System.getProperties();
		pps.list(System.out);
		
	}
}
