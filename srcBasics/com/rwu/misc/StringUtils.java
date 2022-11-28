package com.rwu.misc;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static boolean equalsNullSafe(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}

		if (str1 != null && str2 == null) {
			return false;
		}

		if (str1 == null && str2 != null) {
			return false;
		}

		return str1.equals(str2);
	}

	public static List<Character> toCharList(String string) {
		List<Character> chars = new ArrayList<Character>();

		char[] arr = string.toCharArray();

		for (char curr : arr) {
			chars.add(curr);
		}

		return chars;
	}

	public static String getLimitedString(String in, int len) {
		if (in.length() < len) {
			return in;
		}

		return in.substring(0, len - 3) + "...";
	}

	public static String limitText(String text, int maxLength) {
		if (text == null) {
			return null;
		}

		int length = text.length();
		if (length > maxLength) {
			text = text.substring(0, (maxLength / 2)) + " (...) " + text.substring(length - (maxLength / 2));
		}

		return text;
	}

	public static String limitNewlinesForDisplay(String text, int maxNewlines) {
		if (text == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		int counter = 0;

		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);

			if (ch == '\r') {
				continue;
			}

			if (ch == '\n') {
				counter++;

				if (counter > maxNewlines) {
					continue;
				}
			}

			// Add
			sb.append(ch);
		}

		return sb.toString();
	}

	/**
	 * Caution: case sensitive
	 */
	public static void replaceAll(StringBuffer s, final String searchFor, String replaceWith) {
		if (replaceWith == null) {
			replaceWith = "";
		}

		// Look for first occurrence of searchFor
		int matchIndex = s.indexOf(searchFor);
		if (matchIndex == -1) {
			// No replace operation needs to happen
			return;
		} else {

			// Allocate a StringBuffer that will hold one replacement with a
			// little extra room.
			final int replaceWithLength = replaceWith.length();
			final int searchForLength = searchFor.length();

			int pos = 0;
			do {
				s.replace(matchIndex, matchIndex + searchForLength, replaceWith);

				// Find next occurrence, if any
				pos = matchIndex + replaceWithLength;
				matchIndex = s.indexOf(searchFor, pos);
			} while (matchIndex != -1);
		}
	}

	public static String replaceAllNoRegex(String text, String searchFor, String replaceWith) {
		if (text == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer(text);

		replaceAll(sb, searchFor, replaceWith);

		return sb.toString();
	}

	public static String replaceAtEnd(String text, String searchFor, String replaceWith) {
		if (text.endsWith(searchFor)) {
			return text.substring(0, text.length() - searchFor.length());
		}

		return text;
	}

	/**
	 * Split by line, keep empty lines
	 */
	public static List<Integer> getEmptyLinesIndices(String text) {
		List<String> lines = splitByLineKeepEmpty(text);
		List<Integer> result = new ArrayList<Integer>();

		int i = 0;
		for (String line : lines) {
			if (line.isEmpty()) {
				result.add(i);
			}

			i++;
		}

		return result;
	}

	private static List<String> splitByLineKeepEmpty(String text) {
		return split(text, "\n", false, true);
	}

	public static List<String> split(String text, String separator, boolean trimmed, boolean keepEmptyStrings) {
		List<String> result = new ArrayList<String>();
		if (text == null) {
			return result;
		}

		if (text.isEmpty()) {
			return result;
		}

		String[] splitted = text.split(separator, keepEmptyStrings ? -1 : 0);

		for (String curr : splitted) {
			if (trimmed && curr != null) {
				result.add(curr.trim());
			} else {
				result.add(curr);
			}
		}

		return result;
	}

	/**
	 * Remove line breaks at front and end (ignore spaces)
	 */
	public static String trimNewlinesOnly(String text) {
		int start = 0;
		int end = text.length();

		while (start < text.length() && text.charAt(start) == '\n') {
			start++;
		}

		while (end > start && text.charAt(end - 1) == '\n') {
			end--;
		}

		return text.substring(start, end);
	}

	public static String joinWithSemicolon(List<String> values) {
		return join(null, values, ";", false);
	}

	public static String joinWithComma(List<String> values) {
		return join(null, values, ",", false);
	}

	public static String joinWith(List<String> values, String separator) {
		return join(null, values, separator, false);
	}

	public static String joinWithSpace(String... strings) {
		List<String> values = new ArrayList<>();
		for (String str : strings) {
			values.add(str);
		}

		return join(null, values, " ", false);
	}

	public static boolean isValue(String text) {
		if (text == null || text.isEmpty()) {
			return false;
		}

		return true;
	}

	public static boolean noValue(String text) {
		if (text == null || text.isEmpty()) {
			return true;
		}

		return false;
	}

	public static boolean hasTextContent(String text) {
		int len = text.length();

		for (int i = 0; i < len; i++) {
			char charAt = text.charAt(i);
			if (charAt == ' ' || charAt == '\n' || charAt == '\r' || charAt == '\t') {
				continue;
			}

			return true;
		}

		return false;
	}

	public static String join(StringBuilder sb, List<String> values, String separator, boolean ignoreEmptyStrings) {
		if (values == null || values.isEmpty()) {
			return null;
		}

		boolean hasBuilder = sb != null;

		if (sb == null) {
			sb = new StringBuilder();
		}

		int i = 0;
		for (String value : values) {
			if (value == null) {
				continue;
			}

			if (ignoreEmptyStrings && value.isEmpty()) {
				continue;
			}

			if (i != 0 && separator != null) {
				sb.append(separator);
			}

			sb.append(value);

			i++;
		}

		if (hasBuilder) {
			return null;
		}

		return sb.toString();
	}

	public static int getLineCount(String text, int maximumCount) {
		int counter = 0;
		int offset = 0;

		if (text == null) {
			return 0;
		}

		while (true) {
			if (offset >= text.length()) {
				break;
			}

			int found = text.indexOf("\n", offset);
			if (found == -1) {
				return counter;
			}

			counter++;

			if (counter >= maximumCount) {
				break;
			}

			offset = found + 1;
		}

		return counter;
	}

	public static String getReducedText(String text, int maxLineLength, int maxLines) {
		if (text == null) {
			return null;
		}

		String[] lines = text.split("\n");

		StringBuilder sb = new StringBuilder();

		int i=0;
		for(String line : lines) {
			if (i >= maxLines) {
				sb.append("\n(...)");

				break;
			}

			if (line.length() > maxLineLength) {
				line = line.substring(0, maxLineLength) + " (...)";
			}

			if (sb.length() > 0) {
				sb.append("\n");
			}

			sb.append(line);

			i++;
		}

		return sb.toString();
	}
}
