package com.rwu.fx.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rwu.misc.IntUtils;

import javafx.scene.Node;

public class FxStyleUtils {

	/**
	 * Add style
	 */
	public static void addStyle(Node node, String styleSetting) {
		String style = node.getStyle();
		if (style == null) {
			style = "";
		}

		StringBuilder sb = new StringBuilder();

		sb.append(style);

		// Space
		if (sb.length() > 0) {
			sb.append(" ");
		}

		sb.append(styleSetting);

		String styleStr = sb.toString();
		node.setStyle(styleStr);
	}

	public static void removeStyle(Node node, String styleSetting) {
		String style = node.getStyle();

		String newStyle = removeStyle(style, styleSetting);

		node.setStyle(newStyle);
	}

	public static String removeStyle(String style, String styleSetting) {
		Pattern pattern = Pattern.compile(styleSetting + "[^;]+;");

		StringBuffer sb = new StringBuffer();

		Matcher matcher = pattern.matcher(style);
		while (matcher.find()) {
			matcher.appendReplacement(sb, "");
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	public static int getValueInt(String style, String styleSetting, int defaultValue) {
		Pattern pattern = Pattern.compile(styleSetting + "[^;]+;");

		Matcher matcher = pattern.matcher(style);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();

			String substr = style.substring(start + styleSetting.length() + 1, end - 1);
			substr = substr.trim();

			int result = IntUtils.parse(substr, defaultValue);
			return result;
		}

		return defaultValue;
	}
}
