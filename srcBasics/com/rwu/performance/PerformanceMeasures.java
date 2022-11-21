package com.rwu.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rwu.misc.Pair;
import com.rwu.misc.PairValuesDoubleComparator;

public class PerformanceMeasures {

	private static int checkSizeCounter = 0;

	private static Map<String, PerformanceMeasure> measures = new HashMap<String, PerformanceMeasure>();

	public static void addDuration(String name, int millis) {
		PerformanceMeasure performanceMeasure = measures.get(name);

		if (performanceMeasure == null) {
			performanceMeasure = new PerformanceMeasure();
			measures.put(name, performanceMeasure);
		}

		performanceMeasure.addDuration(millis);

		checkSizeCounter++;

		if (checkSizeCounter > 20) {
			checkMaxSize();

			checkSizeCounter = 0;
		}
	}

	public static int getDuration(String name) {
		PerformanceMeasure performanceMeasure = measures.get(name);
		if (performanceMeasure == null) {
			return -1;
		}

		return (int) performanceMeasure.getSum();
	}

	private static void checkMaxSize() {
		if (measures.size() > 50) {
			//Log.warning("Cleared performance measure", measures.size());

			measures.clear();
		}
	}

	public static String getSummary() {
		StringBuilder builder = new StringBuilder();

		// List of values
		List<Pair<String, Double>> averages = new ArrayList<Pair<String, Double>>();
		for (Entry<String, PerformanceMeasure> entry : measures.entrySet()) {
			String key = entry.getKey();
			Double average = entry.getValue().getAverage();

			averages.add(Pair.get(key, average));
		}

		// Sort values by average duration
		PairValuesDoubleComparator comparator = new PairValuesDoubleComparator(true);
		Collections.sort(averages, comparator);

		for (Pair<String, Double> average : averages) {
			String key = average.getFirst();

			builder.append(measures.get(key).toStringSummary());
			builder.append(": ");
			builder.append(key);
			builder.append("\n");
		}

		return builder.toString();
	}

}
