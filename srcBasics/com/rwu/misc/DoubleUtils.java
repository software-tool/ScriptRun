package com.rwu.misc;

public class DoubleUtils {

	public static Double parse(String text, double defaultValue) {
		Double parsed = null;

		try {
			parsed = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			// Ignore

			return defaultValue;
		}

		return parsed;
	}
}
