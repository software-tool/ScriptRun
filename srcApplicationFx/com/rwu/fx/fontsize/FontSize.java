package com.rwu.fx.fontsize;

import java.util.ArrayList;
import java.util.List;

public class FontSize {

	// Filename of CSS for font size, is used for dialogs
	public static String fontSizeFile = null;

	public static List<String> available = new ArrayList<>();
	static {
		for (int i = 100; i <= 125; i += 5) {
			available.add(i + "");
		}

		for (int i = 140; i <= 250; i += 10) {
			available.add(i + "");
		}

		for (int i = 300; i <= 500; i += 50) {
			available.add(i + "");
		}
	}

	public static String getPrevious(String current) {
		int index = available.indexOf(current);
		index--;

		if (index >= 0) {
			return available.get(index);
		}

		return available.get(0);
	}

	public static String getNext(String current) {
		int index = available.indexOf(current);
		index++;

		if (index < available.size() - 1) {
			return available.get(index);
		}

		return available.get(available.size() - 1);
	}
}
