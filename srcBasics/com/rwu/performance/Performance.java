package com.rwu.performance;

public class Performance {

	private static PerformanceInst INSTANCE = new PerformanceInst();

	public static void setSystemOut(boolean systemOut) {
		INSTANCE.setSystemOut(systemOut);
	}

	@Deprecated
	public static void go(String key) {
		INSTANCE.go(key);
	}

	public static long get(String key) {
		return INSTANCE.get(key);
	}

	public static void duration(String key) {
		INSTANCE.duration(key);
	}

	public static void addDuration(String key, long diff) {
		INSTANCE.addDuration(key, diff);
	}

	public static void endDurations(boolean sorted) {
		INSTANCE.endDurations(sorted);
	}

	public static long end(String key, String... extraText) {
		return INSTANCE.end(key, extraText);
	}

	public static int end() {
		return INSTANCE.end();
	}

	public static void writeSummary(boolean sorted) {
		INSTANCE.writeSummary(sorted);
	}

	public static void writeSummaryEnds(boolean sorted) {
		INSTANCE.writeSummaryEnds(sorted);
	}
}
