package com.github.jadenyangca.analyzer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.jadenyangca.analyzer.interfaces.JSONStringCounterInterface;
import com.github.jadenyangca.analyzer.utils.PropertiesUtil;
/**
 * Use regular expression to analyze JSON string if the format is damaged
 * @author Jaden
 */
public class RegexJSONStringCounter implements JSONStringCounterInterface {
	private Logger logger = LogManager.getLogger();

	@Override
	public boolean calcMetadataType(String filePath, Map<String, Long> numberMap){
		return calcMetadataType(new File(filePath), numberMap);
	}
	
	@Override
	public boolean calcMetadataType(File file, Map<String, Long> numberMap){
		return calaAttribute(file, "metadata-type", numberMap);
	}

	/**
	 * count attribute, ex. "metadata-type": "element" 
	 * @param file JSON file
	 * @param attributeName attribute name in any object of json string
	 * @param numberMap result sorted map
	 * @return executed successfully or not
	 */
	public boolean calaAttribute(File file, String attributeName, Map<String, Long> numberMap) {
		boolean isSuccess = false;
		FileInputStream in = null;
		String str = "";
		
		if(attributeName == null || attributeName.isEmpty()) {
			logger.error("please check attributeType or attributeName which cannot be blank");
			return isSuccess;
		}
		
		try {
			//compare the file size with config
			long size = file.length();
			String maxFileSizeStr = PropertiesUtil.getInstance().getValue("file.size.max of config", "300");
			logger.info("file.size.max: " + maxFileSizeStr);
			if(size > (Long.valueOf(maxFileSizeStr) * 1024 * 1024)) {
				logger.error("The size of JSON file is too big, please split the file into some parts by lines, "
						+ "or directly use fast mode");
				return isSuccess;
			}
			
			in = new FileInputStream(file);
			byte[] buffer = new byte[(int) size];
			in.read(buffer);
			str = new String(buffer, "utf-8");
			
			if(str.isEmpty()) {
				logger.error("The size of JSON file is 0.");
				return isSuccess;
			}
			
			//regular expression, Non-greedy model
			String regexAttr = "\"" + attributeName + "\"(.*?):(.*?)\"(.*?)\"";
			Matcher matcherAttr = Pattern.compile(regexAttr).matcher(str);
			while (matcherAttr.find()) {
				String regStr = matcherAttr.group();
				String typeName = regStr.split(":")[1].trim().replaceAll("\"", "");
				Long number = numberMap.get(typeName);
				
				//key:meta-data type, value: number
				numberMap.put(typeName, number == null ? 1 : (number.longValue() + 1));
			}
			
			isSuccess = true;
		} catch (FileNotFoundException e) {
			logger.error("JSON file is not found.");
		} catch (IOException e) {
			logger.error("Error happened when processing the file.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e);
		}finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return isSuccess;
	}

}
