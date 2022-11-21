package com.rwu.application.lang;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.rwu.application.fx.ApplicationMessage;
import com.rwu.misc.StringUtils;

public class LangHelper {

	public enum Language {
		GERMAN, ENGLISH, SPANISH
	}

	/**
	 * Start-Language without any configuration needed
	 *
	 * On start language.cfg in directory may be read
	 */
	public static Language language = Language.ENGLISH;

	public static Map<Language, String> languageConfigStrings = new HashMap<Language, String>();

	private static Properties prop = new Properties();

	private static Locale LOCALE_ES = new Locale("es", "ES");

	public static boolean SHOW_MISSING_ERRORS = false;

	static {
		languageConfigStrings.put(Language.ENGLISH, "English");
		languageConfigStrings.put(Language.SPANISH, "Spanish");
		languageConfigStrings.put(Language.GERMAN, "German");
	}

	public static String get(String key) {
		String found = (String) prop.get(key);

		if (found == null) {
			if (SHOW_MISSING_ERRORS) {
				JOptionPane.showMessageDialog(null, "Text not found: " + key, "Not found", JOptionPane.ERROR_MESSAGE);
			}
			return "";
		}

		return replaceNewlines(found);
	}

	public static String get(String key, String text1) {
		String found = (String) prop.get(key);

		if (found != null) {
			found = StringUtils.replaceAllNoRegex(found, "$1", text1);
		}

		if (found == null) {
			if (SHOW_MISSING_ERRORS) {
				JOptionPane.showMessageDialog(null, "Text not found: " + key, "Not found", JOptionPane.ERROR_MESSAGE);
			}
		}

		return replaceNewlines(found);
	}

	public static String get(String key, String text1, String text2) {
		String found = StringUtils.replaceAllNoRegex((String) prop.get(key), "$1", text1);

		if (found == null) {
			if (SHOW_MISSING_ERRORS) {
				JOptionPane.showMessageDialog(null, "Text not found: " + key, "Not found", JOptionPane.ERROR_MESSAGE);
			}
		}

		return replaceNewlines(StringUtils.replaceAllNoRegex(found, "$2", text2));
	}

	public static String get(String key, int value1) {
		String found = StringUtils.replaceAllNoRegex((String) prop.get(key), "$1", value1 + "");

		if (found == null) {
			if (SHOW_MISSING_ERRORS) {
				JOptionPane.showMessageDialog(null, "Text not found: " + key, "Not found", JOptionPane.ERROR_MESSAGE);
			}
		}

		return replaceNewlines(found);
	}

	public static String get(String key, int value1, int value2) {
		String found = (String) prop.get(key);

		if (found == null) {
			if (SHOW_MISSING_ERRORS) {
				JOptionPane.showMessageDialog(null, "Text not found: " + key, "Not found", JOptionPane.ERROR_MESSAGE);
			}
		}

		StringBuffer sb = new StringBuffer(found);
		StringUtils.replaceAll(sb, "$1", value1 + "");
		StringUtils.replaceAll(sb, "$2", value2 + "");

		return replaceNewlines(sb.toString());
	}

	private static String replaceNewlines(String text) {
		if (text == null) {
			return null;
		}

		return text.replaceAll("\\\\n", "\n").replaceAll("\\n", "\n");
	}

	public static void loadProperties(File file) {
		if (!file.exists()) {
			ApplicationMessage.inst.showErrorMessagePretty("Language file missing",
					"The language file " + file.getAbsolutePath() + " does not exist. Application cannot be started.");
			System.exit(1);
		}

		try {
			prop.load(new FileInputStream(file));
		} catch (Exception e) {
			ApplicationMessage.inst.showErrorMessagePretty("Language file not readable",
					"Cannot read language file " + file.getAbsolutePath() + ". Application cannot be started.\n\nError: " + e.getMessage());
			System.exit(1);
		}
	}

	public static Locale getLocale() {
		if (language == Language.GERMAN) {
			return Locale.GERMAN;
		} else if (language == Language.SPANISH) {
			return LOCALE_ES;
		} else {
			return Locale.ENGLISH;
		}
	}
	
	public static boolean isEnglish() {
		return language == Language.ENGLISH;
	}

	public static LangInfo getLangInfo() {
		return LangInfo.get(language);
	}

	public static Properties getProperties() {
		return prop;
	}

	public static Language getLangSelection(String name) {
		if (name == null) {
			return null;
		}

		return LangInfo.getForConfigKey(name).getLanguage();
	}

	public static String getLangSelection(Language language) {
		if (language == null) {
			return null;
		}

		return LangInfo.get(language).getConfigKey();
	}

	public static String getLangForConfigString(Language language) {
		if (language == null) {
			return null;
		}

		return languageConfigStrings.get(language);
	}
}
