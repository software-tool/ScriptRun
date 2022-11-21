package com.rwu.misc;

public class ValueModel<T> {

	protected T value;

	public ValueModel() {
		value = null;
	}

	public ValueModel(T value) {
		this.value = value;
	}

	public void set(T value) {
		this.value = value;
	}

	public void add(T newValue) {
		if (newValue instanceof Integer) {
			Integer current = (Integer) value;

			current += ((Integer) newValue);

			this.value = (T) current;
		}
	}

	public T get() {
		return value;
	}

	public boolean hasValue() {
		return value != null;
	}

	public void reset() {
		value = null;
	}
}
