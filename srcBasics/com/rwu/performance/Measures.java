package com.rwu.performance;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Measures {
	private static Map<String, Long> startTimes = new HashMap<String, Long>();

	public static boolean ACTIVE = true;

	public static boolean isInactive() {
		return !ACTIVE;
	}

	public static void start(String name) {
		if (isInactive()) {
			return;
		}

		startTimes.put(name, new Date().getTime());
	}

	public static void tick(String name) {
		if (startTimes.containsKey(name)) {
			end(name);
		} else {
			start(name);
		}
	}

	public static void start(String name, File file) {
		if (isInactive()) {
			return;
		}

		String fullName = name + ": " + file.getAbsolutePath();
		start(fullName);
	}

	public static int end(String name) {
		if (isInactive()) {
			return -1;
		}

		Long startTime = startTimes.get(name);

		if (startTime == null) {
			//Log.warning("No start time for", name);
			return -1;
		}

		// Remove
		startTimes.remove(name);

		Integer duration = Long.valueOf(new Date().getTime() - startTime).intValue();

		PerformanceMeasures.addDuration(name, duration);

		return duration;
	}

	public static void end(String name, File file) {
		if (isInactive()) {
			return;
		}

		String fullName = name + ": " + file.getAbsolutePath();
		end(fullName);
	}

	public static int get(String name) {
		return PerformanceMeasures.getDuration(name);
	}
}
