package com.github.jadenyangca.analyzer.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * the Interface for counting metadata types in JSON string
 * @author Jaden
 */
public interface JSONStringCounterInterface {
	/**
	 * to count the total number of metadata types(attribute,constraint,element)
	 * @param filePath JSON string
	 * @param numberMap result sorted map
	 * @return executes successfully or not
	 */
	boolean calcMetadataType(String filePath, Map<String, Long> numberMap);
	/**
	 * to count the total number of metadata types(attribute,constraint,element)
	 * @param file JSON file
	 * @param numberMap result sorted map
	 * @return executed successfully or not
	 */
	boolean calcMetadataType(File file, Map<String, Long> numberMap);
	
	/**
	 * count metadata type in files
	 * @param files file list
	 * @param numberMap result sorted map
	 * @return executed successfully or not
	 */
	default boolean calcMetadataType(List<File> files, Map<String, Long> numberMap) {
		Logger logger = LogManager.getLogger();
		boolean isSuccess = false;
		if(null == files || null == numberMap) {
			logger.error("Please check the parameter which is null");
			return isSuccess;
		}
		for(File file : files) {
			if(!calcMetadataType(file, numberMap)) {
				isSuccess = false;
			}
		}
		return isSuccess;
	}
}
