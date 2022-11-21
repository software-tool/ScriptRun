package com.rwu.misc;

import java.util.Comparator;

/**
 * Pair
 */
public class Pair<T, S> {

	public T first = null;
	public S second = null;

	public Pair() {
	}

	public Pair(T first, S second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public T getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public boolean hasFirst() {
		return first != null;
	}

	public boolean hasSecond() {
		return second != null;
	}

	@Override
	public String toString() {
		return first + ", " + second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null) {
				return false;
			}
		} else if (!first.equals(other.first)) {
			return false;
		}
		if (second == null) {
			if (other.second != null) {
				return false;
			}
		} else if (!second.equals(other.second)) {
			return false;
		}
		return true;
	}

	public boolean hasObject(Object object) {
		if (first != null && first.equals(object)) {
			return true;
		}

		if (second != null && second.equals(object)) {
			return true;
		}

		return false;
	}

	public static <T extends Object, S extends Object> Pair<T, S> get(T first, S second) {
		Pair<T, S> newPair = new Pair<T, S>();

		newPair.first = first;
		newPair.second = second;

		return newPair;
	}

	public static class PairComparator implements Comparator {

		boolean compareFirst;
		boolean sortAsc;
		boolean compareText;

		public PairComparator(boolean compareFirst, boolean compareText, boolean sortAsc) {
			this.compareFirst = compareFirst;
			this.sortAsc = sortAsc;
			this.compareText = compareText;
		}

		@Override
		public int compare(Object first, Object second) {
			Pair<String, String> a = (Pair<String, String>) first;
			Pair<String, String> b = (Pair<String, String>) second;

			int result;

			if (compareText) {
				String left = null;
				String right = null;

				if (compareFirst) {
					left = a.getFirst();
					right = b.getFirst();
				} else {
					left = a.getSecond();
					right = b.getSecond();
				}

				if (left == null) {
					left = "";
				}
				if (right == null) {
					right = "";
				}

				result = left.compareTo(right);
			} else {
				String compareTextA = null;
				String compareTextB = null;
				if (compareFirst) {
					compareTextA = a.getFirst();
					compareTextB = b.getFirst();
				} else {
					compareTextA = a.getSecond();
					compareTextB = b.getSecond();
				}

				if (compareTextA == null) {
					compareTextA = "";
				}
				if (compareTextB == null) {
					compareTextB = "";
				}

				if (compareTextA.length() < compareTextB.length()) {
					result = -1;
				} else if (compareTextA.length() > compareTextB.length()) {
					result = 1;
				} else {
					result = 0;
				}
			}

			if (sortAsc == false) {
				result = -result;
			}

			return result;
		}
	}
}
