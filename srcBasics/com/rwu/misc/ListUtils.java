package com.rwu.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

	public static <T extends Object> List<T> cloneList(List<T> input) {
		if (input instanceof ArrayList) {
			return (List<T>) ((ArrayList<T>) input).clone();
		}

		return null;
	}

	public static <T extends Object> void moveLeft(List<T> input, T entry) {
		int index = input.indexOf(entry);

		index--;
		if (index == -1) {
			// Not possible
			return;
		}

		input.remove(entry);

		input.add(index, entry);
	}

	public static <T extends Object> void moveRight(List<T> input, T entry) {
		int index = input.indexOf(entry);

		index++;
		if (index >= input.size()) {
			// Not possible
			return;
		}

		input.remove(entry);

		input.add(index, entry);
	}

	public static <T extends Object> T getFirst(List<T> list) {
		if (list.isEmpty()) {
			return null;
		}

		return list.get(0);
	}

	public static <T extends Object> T getLast(List<T> list) {
		if (list.isEmpty()) {
			return null;
		}

		return list.get(list.size() - 1);
	}

	public static <T extends Object> boolean isEqual(List<T> list1, List<T> list2) {
		if (list1.size() != list2.size()) {
			return false;
		}

		int i = 0;
		for (T current : list1) {
			if (!current.equals(list2.get(i))) {
				return false;
			}

			i++;
		}

		return true;
	}

	public static <T extends Object> Map<T, Integer> getAsMapWithIndices(List<T> list) {
		Map<T, Integer> result = new HashMap<>();

		int i = 0;
		for (T entry : list) {
			result.put(entry, i);

			i++;
		}

		return result;
	}

	/**
	 * Add element below other element
	 */
	public static <T extends Object> void addBelow(List<T> list, T newEntry, T refEntry) {
		for (int i = 0; i < list.size(); i++) {
			T current = list.get(i);

			if (current == refEntry) {
				list.add(i + 1, newEntry);
				return;
			}
		}

		// Fallback: At end
		list.add(newEntry);
	}

	public static <T extends Object> void addIfNotExisting(List<T> base, List<T> newItems) {
		for (T newItem : newItems) {
			if (!base.contains(newItem)) {
				base.add(newItem);
			}
		}
	}

	public static <T extends Object> void addIfNotExisting(List<T> base, T item) {
		if (!base.contains(item)) {
			base.add(item);
		}
	}

	public static void sortList(List base, List order) {
		Comparator<Object> comparator = new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				int index1 = order.indexOf(o1);
				int index2 = order.indexOf(o2);

				if (index1 == index2) {
					return 0;
				}

				if (index1 < index2) {
					return -1;
				}

				return 1;
			}
		};

		Collections.sort(base, comparator);
	}
}
