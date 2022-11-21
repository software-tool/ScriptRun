package com.rwu.misc;

public class YesModel extends ValueModel<Boolean> {

	public YesModel() {
		super(false);
	}

	public YesModel(boolean value) {
		super(value);
	}

	public boolean isTrue() {
		return value == true;
	}

	public boolean isFalse() {
		return value == false;
	}

	public void setFalse() {
		value = false;
	}
}
