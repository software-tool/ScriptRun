package com.rwu.misc;

public class HtmlUtils {

	public static String escape(String text) {
		return escape(text, true);
	}

	/**
	 * Escape for HTML
	 * 
	 * @param fromAscii127 If true, replace all above ASCII 126, too
	 */
	public static String escape(String text, boolean fromAscii127) {
		if (text == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				if (fromAscii127 && c > 0x7e) {
					// Bigger than ASCII 126 -> Hexcode
					// 126: "~"
					// 127: "DEL"

					sb.append("&#" + ((int) c) + ";");
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

}
