package com.github.jadenyangca.analyzer.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.jadenyangca.analyzer.impl.RegexJSONStringCounter;
import com.github.jadenyangca.analyzer.utils.CounterUtil;

/**
 * test cases for RegexJSONStringCounter
 * @author Jaden
 */
public class RegexJSONStringCounterTest {

	@BeforeEach
	public void processFile() {
		// TODO process JSON files before test
	}

	/**
	 * test the regex mode with the JSON file which format is damaged.
	 */
	@Test
	public void testCalcMetadataTypeDamaged() {
		// load damaged file
		String filePath = getClass().getResource("/sample_damaged.json").getFile();
		Map<String, Long> map = CounterUtil.getSortedMap();
		// attribute: 148, constraint: 166, element: 201
		assertEquals(true, new RegexJSONStringCounter().calcMetadataType(filePath, map));
		CounterUtil.printMap(map);
		assertEquals(Long.valueOf(148).longValue(), map.get("attribute") == null ? 0 : map.get("attribute").longValue(), "attribute: 148");
		assertEquals(Long.valueOf(166).longValue(), map.get("constraint") == null ? 0 : map.get("constraint").longValue(), "constraint: 166");
		assertEquals(Long.valueOf(201).longValue(), map.get("element") == null ? 0 : map.get("element").longValue(), "element: 201");
	}

	/**
	 * test the regex mode with regular JSON file.
	 */
	@Test
	public void testCalcMetadataType() {
		// load file
		String filePath = getClass().getResource("/sample.json").getFile();
		Map<String, Long> map = CounterUtil.getSortedMap();
		// attribute: 148, constraint: 166, element: 201
		assertEquals(true, new RegexJSONStringCounter().calcMetadataType(filePath, map));
		CounterUtil.printMap(map);
		assertEquals(Long.valueOf(148).longValue(), map.get("attribute") == null ? 0 : map.get("attribute").longValue(), "attribute: 148");
		assertEquals(Long.valueOf(166).longValue(), map.get("constraint") == null ? 0 : map.get("constraint").longValue(), "constraint: 166");
		assertEquals(Long.valueOf(201).longValue(), map.get("element") == null ? 0 : map.get("element").longValue(), "element: 201");
	}

	/**
	 * test the empty file
	 */
	@Test
	public void testEmptyFile() {
		// load damaged file
		String filePath = getClass().getResource("/empty.json").getFile();
		Map<String, Long> map = CounterUtil.getSortedMap();
		// attribute: 148, constraint: 166, element: 201
		assertEquals(false, new RegexJSONStringCounter().calcMetadataType(filePath, map));
		CounterUtil.printMap(map);
	}
}
