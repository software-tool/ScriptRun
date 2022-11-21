package com.rwu.application.config;

import java.util.ArrayList;
import java.util.List;

public class AppConfigUtil {

	/**
	 * Read a list
	 * 
	 * [nodeName]/[prefix]1 = "aaa"
	 * [nodeName]/[prefix]2 = "bbb"
	 * [nodeName]/[prefix]3 = "ccc"
	 */
	public static List<String> readList(String nodeName, String prefix, int maxSize, boolean counterWithZero) {
		List<String> list = new ArrayList<>();

		for (int i = 1; i < maxSize; i++) {
			String param = getName(prefix, i, counterWithZero);

			String value = AppConfig.getValue(nodeName, param);
			if (value == null) {
				break;
			}

			list.add(value);
		}

		return list;
	}

	/**
	 * Write a list
	 * 
	 * Existing and no longer used entries in max range will be deleted
	 * 
	 * [nodeName]/[prefix]1 = "aaa"
	 * [nodeName]/[prefix]2 = "bbb"
	 * [nodeName]/[prefix]3 = "ccc"
	 */
	public static void storeList(String nodeName, String prefix, List<String> list, int maxSize, boolean counterWithZero) {
		for (int i = 0; i < list.size(); i++) {
			int pos = i + 1;

			String param = getName(prefix, pos, counterWithZero);
			String entry = list.get(i);

			AppConfig.setValue(nodeName, param, entry);
		}

		int deleteStart = list.size() + 1;

		for (int i = deleteStart; i < maxSize; i++) {
			String param = getName(prefix, i, counterWithZero);

			String value = AppConfig.getValue(nodeName, param);
			if (value == null) {
				// No value

				break;
			}

			// Delete
			AppConfig.resetValue(nodeName, param);
		}
	}

	private static String getName(String prefix, int index, boolean counterWithZero) {
		String start = prefix + index;
		if (counterWithZero && index < 10) {
			start = prefix + "0" + index;
		}
		return start;
	}
}
