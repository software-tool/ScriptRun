package com.rwu.misc;

public class ValueUtil {

	public static boolean isValue(String value) {
		if (value == null) {
			return false;
		}

		if (value.isEmpty()) {
			return false;
		}

		return true;
	}
}
