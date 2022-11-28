package com.rwu.misc;

import com.rwu.io.UnicodeReader;
import com.rwu.log.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIOUtils {

	public static void safelyDeleteFile(File file) {
		try {
			file.delete();
		} catch (Exception e) {
			Log.warn("Error on deletion: " + file.getAbsolutePath() + ", " + e.getMessage());
		}
	}

	/**
	 * Read file
	 */
	public static String readFile(File file) throws IOException {
		byte[] bytes = readFileBytes(file);
		if (bytes == null) {
			return null;
		}

		return new String(bytes);
	}

	/**
	 * Read bytes of file
	 */
	public static byte[] readFileBytes(String file) throws IOException {
		return readFileBytes(new File(file));
	}

	/**
	 * Read bytes of file
	 */
	public static byte[] readFileBytes(File file) throws IOException {
		if (!file.exists()) {
			//Log.warning("File to read bytes does not exist", file);
			return null;
		}

		// Open file
		try (RandomAccessFile fileAccess = new RandomAccessFile(file, "r")) {
			// Get and check length
			long longlength = fileAccess.length();
			int length = (int) longlength;
			if (length != longlength) {
				throw new IOException("File size >= 2 GB");
			}

			// Read file and return data
			byte[] data = new byte[length];
			fileAccess.readFully(data);
			return data;
		}
	}

	public static void writeToFile(String text, File file, String encoding, boolean writeBom) throws IOException {
		if (text == null) {
			Exception ex = new Exception("text == null");
			Log.error("Utils.writeToFile", ex);
			return;
		}

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter out = null;
		try {
			fos = new FileOutputStream(file);

			if (encoding == null) {
				osw = new OutputStreamWriter(fos);
			} else {
				osw = new OutputStreamWriter(fos, encoding);
			}

			out = new BufferedWriter(osw);

			if (writeBom) {
				if ("UTF-8".equals(encoding)) {
					fos.write(UnicodeReader.BOM.UTF_8.getBytes());
				} else if ("UTF-16".equals(encoding)) {
					//fos.write(UnicodeReader.BOM.UTF_16_LE.getBytes());
					//fos.write(UnicodeReader.BOM.UTF_16_LE.getBytes());
				}
			}

			out.write(text);
			out.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				Log.error(ex);
			}

			try {
				if (osw != null) {
					osw.close();
				}
			} catch (IOException ex) {
				Log.error(ex);
			}

			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ex) {
				Log.error(ex);
			}
		}
	}

	public static List<String> getFileAsArray(File file, String encoding) {
		ArrayList<String> ret = new ArrayList();

		try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);) {
			while (br.ready()) {
				ret.add(new String(br.readLine().getBytes(), encoding));
			}
		} catch (FileNotFoundException e1) {
			Log.warn("Exception in Utils.getFileAsArray: " + e1);
		} catch (IOException e1) {
			Log.warn("Exception in Utils.getFileAsArray: " + e1);
		}

		return ret;
	}
}
