package com.rwu.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.rwu.log.Log;
import xml.LogHandler;

public class Utils {

	public static byte[] getBytes(File file) throws IOException {
		FileInputStream fileinputstream = new FileInputStream(file);

		int numberBytes = fileinputstream.available();
		byte bytearray[] = new byte[numberBytes];

		fileinputstream.read(bytearray);

		fileinputstream.close();

		return bytearray;
	}

	public static String getFile(File file, String encoding) throws Exception {
		return getFile(file, encoding, "\n");
	}

	public static String getFile(File file, String encoding, String lineSeparator) throws Exception {
		if (!file.exists()) {
			return null;
		}

		try {
			String fileText = getFile(file, -1, lineSeparator);
			if (fileText == null) {
				return null;
			}

			if (encoding == null) {
				return fileText;
			} else {
				return new String(fileText.getBytes(), encoding);
			}
		} catch (UnsupportedEncodingException ex) {
			Log.error(ex);
			return null;
		}
	}

	public static String getFile(File file, int numberOfLines, String lineSeparator) throws Exception {
		StringBuilder sb = new StringBuilder();

		int linecounter = 0;

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while (br.ready()) {
				sb.append(br.readLine());

				if (br.ready()) {
					sb.append(lineSeparator);
				}

				if (numberOfLines != -1) {
					linecounter++;

					if (linecounter >= numberOfLines) {
						break;
					}
				}
			}
			br.close();
			fr.close();
		} catch (Exception e1) {
			throw e1;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (Exception ex) {
				throw ex;
			}
		}

		return sb.toString();
	}

	public static boolean equalsNullSafe(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}

		if (obj1 == null) {
			return false;
		}
		if (obj2 == null) {
			return false;
		}

		return obj1.equals(obj2);
	}

	public static List<Pair<String, String>> readLines(String in) {
		ArrayList<Pair<String, String>> result = new ArrayList<Pair<String, String>>();

		if (in == null) {
			return result;
		}

		String[] lines = in.split("\n");
		for (String line : lines) {
			if (line.trim().length() == 0) {
				continue;
			}

			int index = line.indexOf('=');
			if (index == -1) {
				result.add(new Pair<String, String>(line, null));
			} else {
				String name = line.substring(0, index).trim();
				String text = line.substring(index + 1);

				if (name.isEmpty()) {
					continue;
				}

				result.add(new Pair<String, String>(name, text));
			}
		}

		return result;
	}

	public static List<String> getLines(String text) {
		List<String> found = new ArrayList<String>();
		if (text == null) {
			return found;
		}

		String[] lines = text.split("\n");

		for (String curr : lines) {
			curr = curr.trim();

			if (curr.length() > 0) {
				found.add(curr);
			}
		}

		return found;
	}

	public static void writeToFile(String text, File file, String encoding) throws IOException {
		// System.out.println("writeToFile: " + file + ", " + encoding);

		if (text == null) {
			Log.warn("Utils.writeToFile: text == null");
			return;
		}

		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			out.write(text);
			out.close();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String getExceptionShortText(Exception e) {
		String message = e.getMessage();
		if (message == null) {
			message = e.toString();
		}

		return message;
	}

	/**
	 * Stacktrace as String
	 */
	public static String getStacktraceString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);

		return sw.toString();
	}

}
