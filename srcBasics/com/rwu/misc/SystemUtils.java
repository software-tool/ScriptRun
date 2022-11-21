package com.rwu.misc;

import com.rwu.log.Log;
import xml.LogHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class SystemUtils {

	/**
	 * Open URL (System-Browser)
	 */
	public static void openUrl(String url) {
		try {
			URI uri = new URI(url);
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException ex) {
			Log.error(ex);
		} catch (IOException ex) {
			Log.error(ex);
		}
	}

	public static void openFile(File file) {
		if (!file.exists()) {
			return;
		}

		try {
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Log.warn("Cannot open file", ex);
		}
	}

	public static void openDirectory(File dir) {
		if (!dir.exists()) {
			return;
		}

		try {
			Desktop.getDesktop().open(dir);
		} catch (IOException ex) {
			if (AppBasic.isNotMac) {
				Log.warn("Cannot open directory", ex);
			}

			// Mac: May not work for protected directories
			// Fallback, Mac
			Desktop.getDesktop().browseFileDirectory(dir);
		}
	}

	public static void openFileInBrowser(File file) {
		try {
			URI uri = file.toURI();

			Desktop.getDesktop().browse(uri);
		} catch (IOException ex) {
			Log.error(ex);
		}
	}

	public static String getSystemInformation(boolean xmlEditor, File runtimeDir) {
		StringBuilder sb = new StringBuilder();

		int mb = 1024 * 1024;
		Runtime runtime = Runtime.getRuntime();
		long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / mb;
		long freeMemory = runtime.freeMemory() / mb;
		long totalMemory = runtime.totalMemory() / mb;
		long maximumMemory = runtime.maxMemory() / mb;

		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> arguments = runtimeMxBean.getInputArguments();

		File programDirectory = new File(System.getProperty("user.dir"));
		File userDirectory = new File(System.getProperty("user.home"));

		File launch4jFile = new File(programDirectory, "easyxmleditor.l4j.ini");
		File startupIni = new File(userDirectory, ".easyxmleditor/startup.ini");

		File userDir = new File(System.getProperty("user.dir"));

		for (String argument : arguments) {
			sb.append("Program argument: " + argument + "\n");
		}
		sb.append("Java-Version: " + System.getProperty("java.version") + "\n");
		sb.append("Java-Home: " + System.getProperty("java.home") + "\n");
		sb.append("Java-Vendor: " + System.getProperty("java.vendor") + "\n");
		sb.append("OS-Arch: " + System.getProperty("os.arch") + "\n");
		sb.append("OS-Name: " + System.getProperty("os.name") + "\n");
		sb.append("OS-Version: " + System.getProperty("os.version") + "\n");
		sb.append("User-Dir: " + System.getProperty("user.dir") + "\n");
		sb.append("User-Home: " + System.getProperty("user.home") + "\n");
		sb.append("User-Name: " + System.getProperty("user.name") + "\n");

		sb.append("Userprofile: " + System.getenv("USERPROFILE") + "\n");

		sb.append("JavaFX Version: " + System.getProperty("javafx.runtime.version") + "\n");

		if (xmlEditor) {
			if (launch4jFile.exists()) {
				sb.append("Ini-File Exists: " + launch4jFile.getAbsolutePath() + "\n");

				List<String> lines = FileIOUtils.getFileAsArray(launch4jFile, "UTF-8");
				for (String line : lines) {
					sb.append("  Ini: " + line + "\n");
				}
			} else {
				sb.append("Ini-File Not Present: " + launch4jFile.getAbsolutePath() + "\n");
			}

			if (startupIni.exists()) {
				sb.append("Startup Ini-File Exits: " + startupIni.getAbsolutePath() + "\n");

				List<String> lines = FileIOUtils.getFileAsArray(startupIni, "UTF-8");
				for (String line : lines) {
					sb.append("  Startup-Ini: " + line + "\n");
				}
			} else {
				sb.append("Startup-Ini-File Not Present: " + startupIni.getAbsolutePath() + "\n");
			}
		}

		sb.append("\n");
		sb.append("----------- Directories ------------" + "\n");
		sb.append("Working directory: " + new File(".").getAbsolutePath() + "\n");

		if (runtimeDir != null) {
			sb.append("Runtime directory: " + runtimeDir.getAbsolutePath() + "\n");
		}

		sb.append("User-Dir (user.dir): " + userDir + "\n");
		sb.append("User-Home (user.home): " + userDirectory + "\n");
		sb.append("Userprofile (env: USERPROFILE): " + System.getenv("USERPROFILE") + "\n");
		sb.append("Env: ALLUSERSPROFILE: " + System.getenv("ALLUSERSPROFILE") + "\n");
		sb.append("Env: APPDATA: " + System.getenv("APPDATA") + "\n");
		sb.append("Env: LOCALAPPDATA: " + System.getenv("LOCALAPPDATA") + "\n");
		sb.append("User-Tmpdir (java.io.tmpdir): " + System.getProperty("java.io.tmpdir") + "\n");

		sb.append("\n");
		sb.append("----------- Memory Information ------------" + "\n");
		sb.append("Total Memory: " + totalMemory + "\n");
		sb.append("Used Memory: " + usedMemory + "\n");
		sb.append("Free Memory: " + freeMemory + "\n");
		sb.append("Maximum Memory: " + maximumMemory + "\n");

		return sb.toString();
	}

	/**
	 * Reduces size of log file, as needed
	 *
	 * @param mbReducing Count of MB when it is reduced
	 * @param kbReduced Target size KB on reduce
	 */
	public static void ensureMaximumLogFileSize(File logFile, int mbReducing, int kbReduced) {
		if (!logFile.exists()) {
			return;
		}

		String ENCODING = "ISO-8859-1";

		long size = logFile.length();

		int BYTES_KB = 1024;
		int BYTES_MB = (1024 * 1024);

		int lengthToReduce = BYTES_MB * mbReducing;
		int lengthReduced = BYTES_KB * kbReduced;

		if (size > lengthToReduce) {
			// Reduce

			String content = null;
			try {
				content = Utils.getFile(logFile, ENCODING);
			} catch (Exception e) {
				Log.warn("Cannot read log file: " + logFile.toString(), e);
			}

			if (content != null) {
				// Make shorter
				content = content.substring(content.length() - lengthReduced);

				try {
					Utils.writeToFile(content, logFile, ENCODING);
				} catch (IOException e) {
					Log.warn("Cannot write reduced log file: " + logFile.toString(), e);
				}
			}
		}
	}

}
