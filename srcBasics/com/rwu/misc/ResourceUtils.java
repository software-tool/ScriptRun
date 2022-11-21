package com.rwu.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.rwu.log.Log;
import xml.LogHandler;

/**
 * Util for Java-Ressources
 */
public class ResourceUtils {
	
	/**
	 * Resource as Text
	 */
	public static String getResource(Class<?> clazz, String path) throws IOException {
		StringBuilder sb = new StringBuilder();

		try (InputStream is = clazz.getResourceAsStream(path)) {
			int value = is.read();
			while (value != -1) {
				sb.append((char) value);

				value = is.read();
			}
		}

		return sb.toString();
	}

	/**
	 * Resource as Text, Set newline
	 */
	public static String getResource(Class<?> clazz, String filename, String newline) {
		try (InputStream inputStream = clazz.getResourceAsStream(filename)) {
			
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			
			while ((str = reader.readLine()) != null) {
				sb.append(str);
				sb.append(newline);
			}

			return sb.toString();
		} catch (Exception e) {
			Log.error("Failed to read resource", e);
		}

		return null;
	}
}
