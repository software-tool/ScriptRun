package com.rwu.misc;

public class TimeModel extends ValueModel<Long> {

	public TimeModel() {
		super(null);
	}

	@Override
	public Long get() {
		if (value == null) {
			return -1l;
		}

		return value;
	}

	public void setNow() {
		set(System.currentTimeMillis());
	}

	public boolean isNewerThen(long other) {
		return get() > other;
	}

	public boolean isOlderThen(long other) {
		return get() < other;
	}
}
