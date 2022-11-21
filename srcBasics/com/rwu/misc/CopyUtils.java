package com.rwu.misc;

import com.rwu.log.Log;

import java.io.*;

public class CopyUtils {

	public static void migrateToNewDirectory(File src, File dest) throws IOException {
		boolean srcExists = src.exists();
		boolean destExists = dest.exists();

		if ((destExists && dest.isDirectory() == false) || (srcExists && src.isDirectory() == false)) {
			throw new RuntimeException("Not directories: " + src.getAbsolutePath() + ", " + dest.getAbsolutePath());
		}

		dest.mkdirs();

		File[] files = src.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			File currDest = new File(dest, file.getName());

			if (file.isDirectory()) {
				migrateToNewDirectory(file, currDest);
			}

			boolean doCopy = false;
			if (currDest.exists()) {
				long modifiedSrc = file.lastModified();
				long modifiedDest = currDest.lastModified();

				if (modifiedSrc > modifiedDest) {
					doCopy = true;
				}
			} else {
				doCopy = true;
			}

			if (doCopy) {
				copyFile(file, currDest);
			}
		}
	}

	public static void migrateFile(File src, File dest) throws IOException {
		boolean srcExists = src.exists();
		boolean destExists = dest.exists();

		if ((destExists && dest.isFile() == false) || (srcExists && src.isFile() == false)) {
			throw new RuntimeException("Not files: " + src.getAbsolutePath() + ", " + dest.getAbsolutePath());
		}

		dest.getParentFile().mkdirs();

		if (srcExists && destExists == false) {
			copyFile(src, dest);
		}
	}

	public static void copyFile(File src, File dest) throws IOException {
		if (src.exists() == false) {
			Log.warn("Could not copy: " + src.getAbsolutePath() + ", " + dest.getAbsolutePath());
			return;
		}

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src)); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));) {
			int read = bis.read();
			while (read != -1) {
				bos.write(read);
				read = bis.read();
			}
		}
	}

	public static void copyResourceToFile(Class<?> clazz, String resourcePath, File dest) throws IOException {
		if (dest.exists()) {
			// No not overwrite any file
			return;
		}

		File parent = dest.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		try (InputStream is = clazz.getResourceAsStream(resourcePath); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));) {
			if (is == null) {
				Log.warn("Cannot read resource: " + clazz + ", " + resourcePath);
				return;
			}

			int read = is.read();
			while (read != -1) {
				bos.write(read);
				read = is.read();
			}
		}
	}
}
