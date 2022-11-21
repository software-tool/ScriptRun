package com.rwu.misc;

public class IntUtils {

	public static Integer parse(String text) {
		Integer parsed = null;

		try {
			parsed = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			// Ignore
		}

		return parsed;
	}

	public static Integer parse(String text, Integer defaultValue) {
		Integer parsed = null;

		try {
			parsed = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			// Default value

			return defaultValue;
		}

		return parsed;
	}
}
