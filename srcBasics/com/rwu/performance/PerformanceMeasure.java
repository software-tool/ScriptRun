package com.rwu.performance;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class PerformanceMeasure {

	private int checkSizeCounter = 0;

	private LinkedHashMap<Long, Integer> millis = new LinkedHashMap<Long, Integer>();

	public void addDuration(Integer milli) {
		millis.put(new Date().getTime(), milli);

		checkSizeCounter++;

		if (checkSizeCounter > 20) {
			checkSize();

			checkSizeCounter = 0;
		}
	}

	private void checkSize() {
		int size = millis.size();

		if (size > 1000) {
			int i = 0;

			LinkedHashMap<Long, Integer> copyMap = new LinkedHashMap<Long, Integer>();

			copyMap.putAll(millis);

			//LogHandler.debug("Remove performance values", size);

			for (Entry<Long, Integer> entry : copyMap.entrySet()) {
				millis.remove(entry.getKey());

				if (i > 800) {
					break;
				}

				i++;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		for (Entry<Long, Integer> entry : millis.entrySet()) {
			Date dateValue = new Date(entry.getKey());

			builder.append(dateValue);
			builder.append(": ");
			builder.append(entry.getValue());
			builder.append("\n");
		}

		builder.append("]");
		return builder.toString();
	}

	public double getAverage() {
		Long sum = 0l;

		for (Entry<Long, Integer> entry : millis.entrySet()) {
			sum += entry.getValue();
		}

		double average = (sum * 1.0) / millis.size();
		return average;
	}

	public long getSum() {
		Long sum = 0l;

		for (Entry<Long, Integer> entry : millis.entrySet()) {
			sum += entry.getValue();
		}

		return sum;
	}

	public String toStringSummary() {
		StringBuilder builder = new StringBuilder();

		double average = getAverage();

		builder.append("Average: " + DoubleRenderer.render2Digits(average));
		builder.append(", Values: " + millis.size());

		return builder.toString();
	}
}
