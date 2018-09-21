package github.com.jadenyangca.analyzer.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import github.com.jadenyangca.analyzer.utils.CounterUtil;

/**
 * test cases for FastJSONStringCounter
 * @author Jaden
 */
public class FastJSONStringCounterTest {

	@BeforeEach
	public void processFile() {
		// TODO process JSON files before test
	}

	/**
	 * test the fast mode with regular JSON file.
	 */
	@Test
	public void testCalcMetadataType() {
		//load fiel
		String filePath = getClass().getResource("/sample.json").getFile();
		Map<String, Long> map = CounterUtil.getSortedMap();
		assertEquals(true, new FastJSONStringCounter().calcMetadataType(filePath, map));
		CounterUtil.printMap(map);
		assertEquals(Long.valueOf(148).longValue(), map.get("attribute") == null ? 0 : map.get("attribute").longValue(), "attribute: 148");
		assertEquals(Long.valueOf(166).longValue(), map.get("constraint") == null ? 0 : map.get("constraint").longValue(), "constraint: 166");
		assertEquals(Long.valueOf(201).longValue(), map.get("element") == null ? 0 : map.get("element").longValue(), "element: 201");
	}

	/**
	 * test the fast mode with the JSON file which format is damaged.
	 */
	@Test
	public void testCalcMetadataTypeDamaged() {
		//load file
		String filePath = getClass().getResource("/sample_damaged.json").getFile();
		Map<String, Long> map = CounterUtil.getSortedMap();
		// parsing error
		assertEquals(false, new FastJSONStringCounter().calcMetadataType(filePath, map));
		CounterUtil.printMap(map);
		assertEquals(0, map.get("attribute") == null ? 0 : map.get("attribute").longValue(), "attribute: 0");
		assertEquals(0, map.get("constraint") == null ? 0 : map.get("constraint").longValue(), "constraint: 0");
		assertEquals(0, map.get("element") == null ? 0 : map.get("element").longValue(), "element: 0");
	}
	
	/**
	 * test the empty file
	 */
	@Test
	public void testEmptyFile() {
		String filePath = getClass().getResource("/empty.json").getFile();
		Map<String, Long> map = CounterUtil.getSortedMap();
		assertEquals(false, new FastJSONStringCounter().calcMetadataType(filePath, map));
		CounterUtil.printMap(map);
	}
}
