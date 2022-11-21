package com.rwu.misc;

public class BooleanModel extends ValueModel<Boolean> {

	public BooleanModel(Boolean value) {
		super(value);
	}

	public BooleanModel() {
		super(false);
	}

	public void setTrue() {
		value = true;
	}

	public void yes() {
		value = true;
	}

	public void setFalse() {
		value = false;
	}

	public void no() {
		value = false;
	}

	public boolean isYes() {
		return get() == true;
	}

	public boolean isNo() {
		return get() == false;
	}
}
