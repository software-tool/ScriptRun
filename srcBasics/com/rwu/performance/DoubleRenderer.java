package com.rwu.performance;

import java.text.NumberFormat;

public class DoubleRenderer {

	private static final NumberFormat format1Digit = NumberFormat.getNumberInstance();
	private static final NumberFormat format2Digits = NumberFormat.getNumberInstance();

	static {
		// One digit after comma
		format1Digit.setMaximumFractionDigits(1);

		// Two digits after comma
		format2Digits.setMaximumFractionDigits(2);
	}

	public static String render2Digits(Double value) {
		if (value == null) {
			return null;
		}

		return format2Digits.format(value);
	}

	public static String render1Digit(Double value) {
		if (value == null) {
			return null;
		}

		return format1Digit.format(value);
	}
}
