package com.rwu.misc;

import com.rwu.log.Log;

import java.util.ArrayList;
import java.util.List;

public class JavaUtils {

	/**
	 * Gets file list of directory, from content.txt
	 */
	public static List<String> listResourcesOfPath(Class<?> clazz, String path) {
		List<String> result = new ArrayList<>();

		String pathContentTxt = path + "/content.txt";

		try {
			String resource = ResourceUtils.getResource(clazz, pathContentTxt, "\n");
			String[] lines = resource.split("\n");
			for (String line : lines) {
				result.add(line.trim());
			}
		} catch (Exception e) {
			Log.error("Failed to read contents of path", e);
		}

		return result;
	}
}
