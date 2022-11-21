package com.rwu.application.lang.generic;

public class LCommon {

	public static ILangProvider provider = null;

	public static String get(String key) {
		String value = provider.get(key);
		if (value == null || value.isEmpty()) {
			return key;
		}

		return value;
	}

	public static String colon(String key) {
		String value = provider.get(key);
		if (value == null || value.isEmpty()) {
			return key;
		}

		return value + ":";
	}

	public static String get(String key, String parameter1) {
		String value = provider.get(key, parameter1);

		if (value == null || value.isEmpty()) {
			return key;
		}

		return value;
	}

	public static String get(String key, String parameter1, String parameter2) {
		String value = provider.get(key, parameter1, parameter2);

		if (value == null || value.isEmpty()) {
			return key;
		}

		return value;
	}

}
