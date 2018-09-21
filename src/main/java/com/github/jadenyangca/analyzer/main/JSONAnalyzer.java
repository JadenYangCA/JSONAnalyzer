package com.github.jadenyangca.analyzer.main;

import java.io.File;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.jadenyangca.analyzer.factory.JSONStringCounterFactory;
import com.github.jadenyangca.analyzer.interfaces.JSONStringCounterInterface;
import com.github.jadenyangca.analyzer.utils.CounterUtil;

/**
 * This class provides an efficient way to analyze JSON string in the file.
 * @author Jaden
 */
public class JSONAnalyzer {
	/**
	 * @param args arg0- file path of JSON file, arg1- fast or regex 
	 * 			   fast - Use FastJSON to count metadata types efficiently if JSON file is huge, which can avoid java.lang.OutOfMemoryError.
	 *             regex - Use regular expression to analyze JSON string if the format is damaged
	 */
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger();
		
		if (args.length < 2) {
			logger.error("* the 1st argument is file path");
			logger.error("* the 2nd argument must be fast or regex");
			logger.error("** fast  - Use FastJSON to count string efficiently avoiding java.lang.OutOfMemoryError if JSON file is huge.");
			logger.error("** regex - Use regular expression to solve it if the format of JSON string is damaged");
			return;
		}
		
		//key:meta-data type, value: number
		Map<String, Long> map = CounterUtil.getSortedMap();
		
		String filePath = args[0];
		String mod = args[1];
		File file = new File(filePath);
		//file.isFile() && 
		if (file.canRead()) {

			// get counter instance
			JSONStringCounterInterface counter = JSONStringCounterFactory.getInstance(mod);
			if (counter == null) {
				logger.error("* the 2nd argument must be fast or regex, or the configuration is not right.");
				return;
			}

			long begin = System.currentTimeMillis();
			boolean isSucc = true;
			if(file.isFile()) {
				// count the total number of metadata types across all records
				isSucc = counter.calcMetadataType(file, map);
			}else if(file.isDirectory() && file.listFiles().length >0){
				File[] jsonFiles = file.listFiles();
				for(File f:jsonFiles) {
					//the whole program failed if one of files failed
					if(!counter.calcMetadataType(f, map)) {
						isSucc = false;
					}
				}
			}
			
			logger.info("JSONAnalyzer " + (isSucc ? "succeeded" : "failed"));
			long end = System.currentTimeMillis();
			logger.debug("Execution time(ms): " + (end - begin));
		} else {
			logger.error("JSON file path is not right or do not have right to read");
		}

		// print the result
		CounterUtil.printMap(map);
	}

}
