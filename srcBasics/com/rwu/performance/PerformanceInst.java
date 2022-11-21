package com.rwu.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.rwu.misc.StringUtils;

public class PerformanceInst {
	private LinkedHashMap<String, Long> startTimes = new LinkedHashMap<String, Long>();

	private LinkedHashMap<String, Long> ends = new LinkedHashMap<String, Long>();

	private LinkedHashMap<String, Long> durationSums = new LinkedHashMap<String, Long>();
	private LinkedHashMap<String, Integer> durationCounts = new LinkedHashMap<String, Integer>();

	private boolean systemOutOnEnd = false;

	public void setSystemOut(boolean systemOut) {
		this.systemOutOnEnd = systemOut;
	}

	/**
	 * Start/End
	 */
	public void go(String key) {
		if (startTimes.containsKey(key)) {
			end(key);

			startTimes.remove(key);
		} else {
			start(key);
		}
	}

	public long get(String key) {
		return ends.get(key);
	}

	public void duration(String key) {
		Long start = startTimes.get(key);
		if (start == null) {
			start(key);
			return;
		}

		long end = System.currentTimeMillis();
		long diff = end - start;

		addToDuration(key, diff);

		startTimes.remove(key);
	}

	public void addDuration(String key, long diff) {
		addToDuration(key, diff);
	}

	private void addToDuration(String key, long diff) {
		Long durationSum = durationSums.get(key);
		if (durationSum == null) {
			durationSum = 0l;
		}
		durationSum += diff;
		durationSums.put(key, durationSum);

		Integer count = durationCounts.get(key);
		if (count == null) {
			count = 1;
		} else {
			count++;
		}
		durationCounts.put(key, count);
	}

	public void start(String key) {
		long start = System.currentTimeMillis();
		startTimes.put(key, start);
	}

	public void endDurations(boolean sorted) {
		System.out.println("=== Durations:");

		List<String> keys = new ArrayList<String>(durationSums.keySet());
		if (sorted) {
			Collections.sort(keys);
		}

		for (String key : keys) {
			end(key, durationCounts.get(key) + " x");
		}
		System.out.println("===");

		durationSums.clear();
		durationCounts.clear();
	}

	public long end(String key, String... extraText) {
		Long start = startTimes.get(key);
		long end = System.currentTimeMillis();
		long diff = 0;

		if (start != null) {
			diff = end - start;

			ends.put(key, diff);
		} else {
			// Duration

			diff = durationSums.get(key);
		}

		String infoText = StringUtils.joinWithSpace(extraText);
		if (infoText == null) {
			infoText = "";
		} else {
			infoText = " | " + infoText;
		}

		if (systemOutOnEnd) {
			System.out.println("---- " + key + ": " + diff + "ms" + infoText);
		}

		return diff;
	}

	public long start() {
		long millis = System.currentTimeMillis();

		startTimes.put(null, millis);

		return millis;
	}

	public int end() {
		long millis = System.currentTimeMillis();
		return (int) (millis - startTimes.get(null));
	}

	public void applyValues(String key, int count, long duration) {
		durationSums.put(key, duration);
		durationCounts.put(key, count);
	}

	public void writeSummary(boolean sorted) {
		System.out.println(getSummary(sorted));
	}

	public String getSummary(boolean sorted) {
		List<String> keys = new ArrayList<String>(durationSums.keySet());
		if (sorted) {
			Collections.sort(keys);
		}

		int MINUTE = (60 * 1000);

		StringBuilder sb = new StringBuilder();

		for (String key : keys) {
			long sum = durationSums.get(key);
			int count = durationCounts.get(key);

			double average = (sum * 1.0d) / count;

			String sumStr = "";
			if (sum > MINUTE) {
				long minutes = (sum / MINUTE);
				long secondsLong = sum - (MINUTE * minutes);

				double seconds = (secondsLong / 1000.0);

				sumStr = minutes + " min, " + DoubleRenderer.render2Digits(seconds) + " sec";
			} else {
				double seconds = (sum / 1000.0);

				sumStr = DoubleRenderer.render2Digits(seconds) + " sec";
			}

			String msString = sum + "";

			sb.append("Performance \"" + key + "\": \tavg=" + DoubleRenderer.render2Digits(average) + " ms\t" + count + "x, duration-sum=" + sumStr + ", ms=" + msString);
			sb.append("\n");
		}

		// Clear
		startTimes.clear();
		durationSums.clear();
		durationCounts.clear();

		return sb.toString();
	}

	public void writeSummaryEnds(boolean sorted) {
		System.out.println(getSummaryEnds(sorted));
	}

	public String getSummaryEnds(boolean sorted) {
		List<String> keys = new ArrayList<String>(durationSums.keySet());
		if (sorted) {
			Collections.sort(keys);
		}

		StringBuilder sb = new StringBuilder();

		for (String key : keys) {
			Long end = ends.get(key);

			if (end != null) {
				sb.append(key + ": " + end + "ms");
				sb.append("\n");
			}
		}

		// Clear
		ends.clear();

		return sb.toString();
	}

	public double getAverageAll() {
		List<String> keys = new ArrayList<String>(durationSums.keySet());

		double averageSums = 0;

		for (String key : keys) {
			long sum = durationSums.get(key);
			int count = durationCounts.get(key);

			double average = (sum * 1.0d) / count;

			averageSums += average;
		}

		return averageSums / durationCounts.size();
	}

	public LinkedHashMap<String, Long> getDurationSums() {
		return durationSums;
	}

}
