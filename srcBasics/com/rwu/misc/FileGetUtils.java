package com.rwu.misc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileGetUtils {

	/**
	 * Get file for string
	 *
	 * Seems not to create errors, allows characters that should be invalid: * | " \ / ?
	 */
	public static File getFileIfPossible(String fileStr) {
		File result = null;

		try {
			result = new File(fileStr);
		} catch (Exception e) {
			//Log.warning("Error parsing file input", e);

			return null;
		}

		return result;
	}

	/**
	 * Read file, count line breaks
	 */
	public static int getFileLineCount(File file) throws IOException {
		int countLF = 0;
		int countCR = 0;

		try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis);) {
			int c;
			while ((c = bis.read()) != -1) {
				char current = (char) c;

				if (current == '\r') {
					countCR++;
				}
				if (current == '\n') {
					countLF++;
				}
			}
		} catch (IOException e) {
			throw e;
		}

		if (countCR == 0 && countLF == 0) {
			// Not possible

			return -1;
		}

		if (countCR > countLF) {
			return countCR;
		}

		return countLF;
	}
}
