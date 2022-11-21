package com.rwu.application.lang;

import java.io.IOException;
import java.io.InputStream;

/**
 * Lang-Texts
 */
public class Ln {
	public static String get(String key) {
		return LangHelper.get(key);
	}

	public static String dots(String key) {
		return LangHelper.get(key) + "...";
	}

	public static String colon(String key) {
		return LangHelper.get(key) + ":";
	}

	public static String colonSpace(String key) {
		return LangHelper.get(key) + ": ";
	}

	public static String get(String key, String text1) {
		return LangHelper.get(key, text1);
	}

	public static String get(String key, String text1, String text2) {
		return LangHelper.get(key, text1, text2);
	}

	public static String get(String key, int value1) {
		return LangHelper.get(key, value1);
	}

	public static String get(String key, int value1, int value2) {
		return LangHelper.get(key, value1, value2);
	}

	public static String getLangFile(String pathStr) {
		return LangHelper.getLangInfo().getPropertiesFilename(pathStr);
	}

	public static void readFromInputStream(InputStream is) throws IOException {
		LangHelper.getProperties().load(is);
	}
}
