package com.rwu.application.lang;

import java.io.File;

import com.rwu.application.ApplicationEnvironment;
import com.rwu.application.lang.LangHelper.Language;
import com.rwu.log.Log;
import com.rwu.misc.Utils;

public class LanguageCommon {

	private static final String ENCODING_SOURCE_FILES = "ISO-8859-15";

	private static final String LANG_KEY_DE = "de";
	private static final String LANG_KEY_ES = "es";
	private static final String LANG_KEY_EN = "en";

	public static void initLanguageFromInstallation() {
		File file = ApplicationEnvironment.getLanguageDefaultFile();
		if (!file.exists()) {
			return;
		}

		String fileContent = null;
		try {
			fileContent = Utils.getFile(file, ENCODING_SOURCE_FILES);
		} catch (Exception e) {
			Log.warn("Failed to read language configuration: " + e.getMessage());
		}

		if (fileContent != null) {
			fileContent = fileContent.trim();

			if (LANG_KEY_DE.equals(fileContent)) {
				// German

				LangHelper.language = Language.GERMAN;
			} else if (LANG_KEY_ES.equals(fileContent)) {
				// Spanish

				LangHelper.language = Language.SPANISH;
			} else if (LANG_KEY_EN.equals(fileContent)) {
				// English

				LangHelper.language = Language.ENGLISH;
			}
		}
	}

}
