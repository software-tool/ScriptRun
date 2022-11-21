package com.rwu.misc;

import java.util.Comparator;

public class PairValuesDoubleComparator implements Comparator<Pair<?, Double>> {
	private boolean inversed = false;

	public PairValuesDoubleComparator() {
	}

	public PairValuesDoubleComparator(boolean inversed) {
		this.inversed = inversed;
	}

	@Override
	public int compare(Pair<?, Double> o1, Pair<?, Double> o2) {
		Double value1 = o1 == null ? null : o1.getSecond();
		Double value2 = o2 == null ? null : o2.getSecond();

		int result = compareDouble(value1, value2);

		if (inversed) {
			return -result;
		}

		return result;
	}

	private static int compareDouble(double value1, double value2) {
		if (value1 == 0 && value2 == 0) {
			return 0;
		}

		if (value1 == 0) {
			return -1;
		}
		if (value2 == 0) {
			return 1;
		}

		if (value1 < value2) {
			return -1;
		}
		if (value1 > value2) {
			return 1;
		}

		return 0;
	}
}
