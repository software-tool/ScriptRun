package com.rwu.misc;

import java.io.File;
import java.util.List;

public class FileUtils {

	/**
	 * Returns file that does not exist in directory
	 * 
	 * @param numberSeparatorString Separator, e.g. "-" or ""
	 * @param maxNr Maximum for counter
	 */
	public static File getFileNotExisting(File directory, String name, String ending, String numberSeparatorString, int maxNr) {
		File theFile = null;

		for (int i = 1; i <= maxNr; i++) {
			String filename = null;

			if (i == 1) {
				filename = name + ending;
			} else {
				filename = name + numberSeparatorString + i + ending;
			}

			theFile = new File(directory, filename);

			if (!theFile.exists()) {
				return theFile;
			}
		}

		return null;
	}

	/**
	 * Retruns first file with name/ending that is not in the block list
	 * 
	 * @param numberSeparatorString Separator, e.g. "-" or ""
	 * @param maxNr Maximum for counter
	 */
	public static String getFilenameNotExistingInList(String name, String ending, String numberSeparatorString, int maxNr, List<String> filenamesBlocked) {
		// Invalid characters
		name = FileNameUtils.getValidFilename(name);

		for (int i = 1; i <= maxNr; i++) {
			String filename = null;

			if (i == 1) {
				filename = name + ending;
			} else {
				filename = name + numberSeparatorString + i + ending;
			}

			if (!filenamesBlocked.contains(filename)) {
				// Result
				return filename;
			}
		}

		return null;
	}
}
