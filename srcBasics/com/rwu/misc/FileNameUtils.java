package com.rwu.misc;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileNameUtils {

	/** Not allowed in file names (Read from Windows) */
	private static final List<String> INVALID_CHARACTERS_FILENAME = Arrays.asList(":", "|", "*", "?", "\"", "<", ">");
	private static final List<String> INVALID_CHARACTERS_PATH = Arrays.asList("\\", "/");

	/** Only letters, numbers, etc. */
	private static final String VALID_CHARACTERS_ASCII_FILENAME = "[^a-zA-Z0-9_\\.-]";

	/**
	 * True if the file has any of the endings
	 */
	public static boolean hasEnding(String filename, List<String> endings) {
		int lastIndex = filename.lastIndexOf(".");
		if (lastIndex == -1) {
			return false;
		}

		for (String ending : endings) {
			if (filename.endsWith(ending)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Provided a valid file name
	 */
	public static String getValidFilename(String someName) {
		String filename = someName;

		// Remove invalid characters
		for (String character : INVALID_CHARACTERS_FILENAME) {
			filename = filename.replace(character, "");
		}

		// Do not allow path separators
		for (String character : INVALID_CHARACTERS_PATH) {
			filename = filename.replace(character, "");
		}

		// Remove double spaces
		while (filename.contains("  ")) {
			filename = filename.replace("  ", " ");
		}

		boolean isValidPath = isValidPath(filename);
		if (isValidPath) {
			return filename;
		}

		return getValidFilenameAsciiOnly(filename);
	}

	private static boolean isValidPath(String path) {
		if (path == null || path.isEmpty()) {
			return false;
		}

		try {
			// Check with IO
			Paths.get(path);
		} catch (InvalidPathException e) {
			//Log.warning("Invalid path (Paths IO)", e.getMessage());

			return false;
		}

		File file = new File(path);
		try {
			// Check with File
			file.getCanonicalPath();
		} catch (IOException e) {
			//Log.warning("Invalid path (File)", e.getMessage());

			return false;
		}

		return true;
	}

	private static String getValidFilenameAsciiOnly(String filename) {
		String updated = filename.replaceAll(VALID_CHARACTERS_ASCII_FILENAME, "-");

		while (updated.contains("--")) {
			updated = updated.replaceAll("--", "-");
		}

		return updated;
	}
}
