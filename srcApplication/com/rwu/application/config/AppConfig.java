package com.rwu.application.config;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class AppConfig {

	private static Map<String, Preferences> nodes = new HashMap<>();

	/**
	 * Init
	 */
	public static void initNode(String nodeName, Class<?> packagePath) {
		Preferences preferences = Preferences.userNodeForPackage(packagePath);

		nodes.put(nodeName, preferences);
	}

	public static void setValue(String nodeName, String key, String value) {
		setValue(get(nodeName), key, value);
	}

	/**
	 * Set Boolean: "true", "false"
	 */
	public static void setBooleanValue(String nodeName, String key, Boolean value) {
		Preferences preferences = get(nodeName);

		if (value == null) {
			setValue(preferences, key, null);
			return;
		}

		String valueStr = value + "";
		setValue(preferences, key, valueStr);
	}

	public static void resetValue(String nodeName, String key) {
		setValue(get(nodeName), key, null);
	}

	public static String getValue(String nodeName, String key) {
		Preferences preferences = get(nodeName);
		if (preferences == null) {
			System.out.println("Cannot read preferences for node: " + nodeName);
			return null;
		}

		return getValue(preferences, key);
	}

	/**
	 * Boolean: "true", "false"
	 */
	public static boolean getBooleanValue(String nodeName, String key, boolean defaultValue) {
		String valueStr = getValue(get(nodeName), key);

		if (valueStr == null) {
			return defaultValue;
		}

		if ("true".equals(valueStr)) {
			return true;
		}
		if ("false".equals(valueStr)) {
			return false;
		}

		return defaultValue;
	}

	private static String getValue(Preferences category, String key) {
		return category.get(key, null);
	}

	private static void setValue(Preferences category, String key, String value) {
		if (value == null) {
			category.remove(key);
		} else {
			category.put(key, value);
		}
	}

	private static Preferences get(String nodeName) {
		return nodes.get(nodeName);
	}
}
