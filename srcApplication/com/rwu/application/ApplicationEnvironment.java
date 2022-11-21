package com.rwu.application;

import java.io.File;

public class ApplicationEnvironment {

	private static File applicationDir = new File(System.getProperty("user.dir"));
	private static File userDir = new File(System.getProperty("user.home"));

	public static File getLanguageDefaultFile() {
		return new File(applicationDir, "language.cfg");
	}
}
