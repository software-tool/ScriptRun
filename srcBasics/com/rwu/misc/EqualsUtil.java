package com.rwu.misc;

public class EqualsUtil {

	public static boolean isDifferent(Object obj1, Object obj2) {
		return !isEqual(obj1, obj2);
	}

	public static boolean isEqual(Object obj1, Object obj2) {
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
}
