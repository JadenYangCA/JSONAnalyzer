package com.github.jadenyangca.analyzer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONReader;
import com.github.jadenyangca.analyzer.interfaces.JSONStringCounterInterface;

/**
 * Use FastJSON to count metadata types efficiently if JSON file is huge, which can avoid java.lang.OutOfMemoryError.
 * It leases used the memory when objects are ended. 
 * @author Jaden
 */
public class FastJSONStringCounter implements JSONStringCounterInterface {
	private Logger logger = LogManager.getLogger();
	@Override
	public boolean calcMetadataType(String filePath, Map<String, Long> numberMap){
		return calcMetadataType(new File(filePath), numberMap);
	}

	@Override
	public boolean calcMetadataType(File file, Map<String, Long> numberMap){
		boolean isSuccess = false;
		BufferedReader buffReader = null;
		JSONReader jReader = null;

		try {
			//get JSON string from the file
			buffReader = new BufferedReader(new FileReader(file));
			jReader = new JSONReader(buffReader);
			jReader.startObject();
			while (jReader.hasNext()) {
				String elem1 = jReader.readString();
				
				// "results": []
				if ("results".equals(elem1)) {
					jReader.startArray();
					while (jReader.hasNext()) {
						
						// "results": [{}]
						jReader.startObject();
						while (jReader.hasNext()) {
							String elem2 = jReader.readString();
							
							// "results": [{..."metadata": []}]
							if ("metadata".equals(elem2)) {
								jReader.startArray();
								while (jReader.hasNext()) {
									
									// "results": [{..."metadata": [{}]}]
									Map<String, String> map = new HashMap<String, String>();
									jReader.readObject(map);
									String tmpKey = map.getOrDefault("metadata-type", "");
									Long number = numberMap.get(tmpKey);
									
									//key:meta-data type, value: number
									numberMap.put(tmpKey, number == null ? 1 : (number.longValue() + 1));
								}
								jReader.endArray();
							} else {
								jReader.readString();
							}
						}
						jReader.endObject();
					}
					jReader.endArray();
				} else {
					jReader.readString();
				}
			}
			jReader.endObject();
			
			isSuccess = true;
		} catch (FileNotFoundException e) {
			logger.error("JSON file is not found.");
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (null != buffReader) {
				try {
					buffReader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (null != jReader) {
				jReader.close();
			}
		}
		return isSuccess;
	}

}
