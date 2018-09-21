package github.com.jadenyangca.analyzer.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Jaden
 */
public class CounterUtil {
	/**
	 * get the tree map with Comparator(asc)
	 * @return sorted tree map
	 */
	public static Map<String, Long> getSortedMap() {
		//concurrent tree map
		Map<String, Long> map = new ConcurrentSkipListMap<String, Long>((str1, str2) -> str1.compareTo(str2));
		return map;
	}

	/**
	 * print the result
	 * @param map the tree map with Comparator(asc)
	 */
	public static void printMap(Map<String, Long> map) {
		Logger logger = LogManager.getLogger(CounterUtil.class);
		if(null == map || map.size()== 0) {
			logger.error("result map is null or empty");
		}
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			logger.info(key + ":" + map.get(key));
		}
	}
}
