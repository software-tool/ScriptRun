package script.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rwu.log.Log;
import com.rwu.misc.StringUtils;
import xml.LogHandler;

public class FileUtils {

	/**
	 * Write text in file
	 */
	public static void writeToFile(File file, String text) throws IOException {
		if (text == null) {
			Log.warn("No text to write into file: " + file.getAbsolutePath());
			return;
		}
		if (file == null) {
			Log.warn("No file specified to write into");
			return;
		}

		//File parentAbsoluteFile = file.getAbsoluteFile().getParentFile();
		// mkdirs(parentAbsoluteFile);

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(text.getBytes(StandardCharsets.UTF_8));
		}
	}

	public static String readFile(File file) throws IOException {
		//Path path = Paths.get(file.getAbsolutePath());

		StringBuilder sb = new StringBuilder();

		try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
			 BufferedReader reader = new BufferedReader(fr)) {

			String str;
			while ((str = reader.readLine()) != null) {
				if (sb.length() > 0) {
					sb.append("\n");
				}

				sb.append(str);
			}

			return sb.toString();
		}
	}

	public static String getRelativePath(File file, File basePath) {
		List<String> steps = new ArrayList<>();

		File parent = file.getParentFile();
		for (int i=0; i<50; i++) {
			if (parent == null) {
				break;
			}

			if (parent.equals(basePath)) {
				break;
			}

			steps.add(parent.getName());

			parent = parent.getParentFile();
		}

		if (steps.isEmpty()) {
			return null;
		}

		Collections.reverse(steps);

		return StringUtils.join(null, steps, "/", true);
	}

	public static URL getUrl(File file) {
		try {
			URI uri = file.toURI();
			URL url = uri.toURL();

			return url;
		} catch (MalformedURLException e) {
			Log.error("Failed to get URL from file", e);
		}

		return null;
	}

	/**
	 * Get file for string
	 *
	 * Does not seem to create errors, all invalid characters are accepted: * | " \ / ?
	 */
	public static File getFileIfPossible(String fileStr) {
		File result = null;

		try {
			result = new File(fileStr);
		} catch (Exception e) {
			Log.warn("Error parsing file input", e);
		}

		return result;
	}
}
