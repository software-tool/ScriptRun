
package com.rwu.application.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.rwu.application.lang.LangHelper.Language;

public class LangInfo {

	public static List<LangInfo> langInfos = new ArrayList<LangInfo>();
	static {
		Locale localeEs = new Locale("es", "ES");

		langInfos.add(new LangInfo(Language.ENGLISH, "en", "English", Locale.ENGLISH));
		langInfos.add(new LangInfo(Language.GERMAN, "de", "German", Locale.GERMAN));
		langInfos.add(new LangInfo(Language.SPANISH, "es", "Spanish", localeEs));
	}

	private Language language = null;

	private String isoKey = null;
	private String configKey = null;

	private Locale locale = null;

	public LangInfo(Language language, String isoKey, String configKey, Locale locale) {
		this.language = language;
		this.isoKey = isoKey;
		this.configKey = configKey;
		this.locale = locale;
	}

	public Language getLanguage() {
		return language;
	}

	public String getConfigKey() {
		return configKey;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getIsoKey() {
		return isoKey;
	}

	public String getPropertiesFilename(String pathStr) {
		return pathStr + "/" + isoKey + ".dat";
	}

	public static LangInfo get(Language language) {
		for (LangInfo info : langInfos) {
			if (language == info.language) {
				return info;
			}
		}

		return null;
	}

	public static LangInfo getForConfigKey(String configKey) {
		for (LangInfo info : langInfos) {
			if (info.configKey.equals(configKey)) {
				return info;
			}
		}

		return null;
	}
}
