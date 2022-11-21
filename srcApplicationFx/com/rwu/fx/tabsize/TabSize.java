package com.rwu.fx.tabsize;

import java.util.ArrayList;
import java.util.List;

public class TabSize {

	public static List<String> available = new ArrayList<>();
	static {
		for (int i = 1; i <= 8; i += 1) {
			available.add(i + "");
		}

		for (int i = 10; i <= 16; i += 2) {
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
